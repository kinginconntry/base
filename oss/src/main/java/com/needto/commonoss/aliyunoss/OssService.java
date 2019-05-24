package com.needto.commonoss.aliyunoss;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.*;
import com.needto.common.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * 阿里云存储服务，需要扫描使用
 * @author Administrator
 * @date 2018/5/29 0029
 */
@Service
public class OssService {

    protected final static Logger LOG = LoggerFactory.getLogger(OssService.class);

    /**
     * 毫秒
     * 检查过期时间时，需要当前系统时间减去上次过期时间大于该参数才认为过期时间还未过期；
     * 原因：若没有该间隔设置，当过期时间与当前系统时间相等时，即在即将过期的临界点时，检查信息返回到客户端，这是过期时间已经过期，客户端也无法使用上传信息了
     */
    private final static int DEFAULT_CHECK_INTERVAL = 30000;

    private final static String DEFAULT_ENCODE = "utf-8";

    @Value("${oss.accessId}")
    private String ossAccessid;

    @Value("${oss.accessKey}")
    private String ossAccessKey;

    @Value("${oss.protocol}")
    private String ossProtocol;

    @Value("${oss.endpoint}")
    private String ossEndpoint;

    /**
     * 默认过期时间
     */
    @Value("${oss.tokenExpire}")
    private int defaultExpireTime;

    private OSSClient ossClient;

    @PostConstruct
    public void initMethod() {
        if (ossClient == null) {
            ossClient = new OSSClient(ossEndpoint, ossAccessid, ossAccessKey);
        }
        if(defaultExpireTime <= 0){
            defaultExpireTime = 300;
        }
        if (defaultExpireTime < (DEFAULT_CHECK_INTERVAL/1000)) {
            defaultExpireTime = defaultExpireTime + DEFAULT_CHECK_INTERVAL;
            LOG.debug("oss凭证过期时间过短，已修改为" + defaultExpireTime);
        }
        defaultExpireTime = defaultExpireTime*1000;

        ossProtocol = StringUtils.isEmpty(ossProtocol)?"http": ossProtocol;
        LOG.debug("oss客户端初始化完成");
    }

    private String getHost(String ossBucket){
        return ossProtocol+"://" + ossBucket + "." + ossEndpoint;
    }

    @PreDestroy
    public void beforeDestory() {
        if (ossClient != null) {
            ossClient.shutdown();
        }
        LOG.debug("oss客户端已销毁");
    }

    /**
     * 获取oss直传需要的参数
     * @param bucket oss bucket
     * @param dir
     * @return
     */
    public OssData getOssParam(String bucket, String dir, OssData.CallBack callBack) {
        Assert.validateStringEmpty(dir, "NO_DIR", "dir can not be null");
        OssData ossData = null;
        try {
            long expireEndTime = System.currentTimeMillis() + defaultExpireTime;
            Date expiration = new Date(expireEndTime);
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);

            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(DEFAULT_ENCODE);
            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            ossData = new OssData();
            ossData.setAccessid(ossAccessid);
            ossData.setPolicy(encodedPolicy);
            ossData.setSignature(postSignature);
            ossData.setDir(dir);
            ossData.setHost(getHost(bucket));
            ossData.setExpire(String.valueOf(expireEndTime));
            if(callBack != null){
                ossData.setCallback(BinaryUtil.toBase64String(JSON.toJSONString(callBack).getBytes("utf-8")));
            }
            LOG.debug("上传参数 {}", ossData.toString());
        } catch (Exception e) {
            LOG.error(e.toString());
        }
        return ossData;
    }

    /**
     * 检查oss 是否过期
     *
     * @return
     */
    public boolean checkOssExpire(long oldExpireTime) {
        LOG.debug("检查oss过期时间");
        long currentTime = System.currentTimeMillis();
        if ((oldExpireTime - currentTime) > DEFAULT_CHECK_INTERVAL) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 删除oss上的文件
     * @param ossBucket oss bucket
     * @param urls
     * @return
     */
    public List<String> delete(String ossBucket, List<String> urls) {
        if(CollectionUtils.isEmpty(urls)){
            throw new LogicException("NO_URL", "url can not be null");
        }
        List<String> keys = new ArrayList<>(urls.size());
        urls.forEach((url) -> {
            if(!StringUtils.isEmpty(url)){
                keys.add(url.replace(getHost(ossBucket) + "/", "").trim());
            }
        });
        LOG.debug("删除oss文件 {}", urls.toString());
        Result<List<String>> result;
        DeleteObjectsRequest request = new DeleteObjectsRequest(ossBucket);
        request.setKeys(keys);
        try{
            DeleteObjectsResult deleteObjectsResult = ossClient.deleteObjects(request);
            return deleteObjectsResult.getDeletedObjects();
        }catch (OSSException e){
            LOG.error(e.toString());
            throw new LogicException("", "无法处理删除任务");
        }catch (ClientException e){
            LOG.error(e.toString());
            throw new LogicException("", "无法连接OSS服务器");
        }catch (Exception e){
            LOG.error(e.toString());
            throw new LogicException("", "删除失败");
        }
    }

    /**
     * 上传文件到oss
     * @param ossBucket bucket
     * @param key 文件路径
     * @param in 输入流
     * @param objectMetadata 自定义http头部信息
     */
    public String upload(String ossBucket, String key, InputStream in, ObjectMetadata objectMetadata){
        ossClient.putObject(ossBucket, key, in, objectMetadata);
        String host = getHost(ossBucket);

        if(key.startsWith("/")){
            return host + key;
        }else{
            return host + "/" + key;
        }

    }

    /**
     * 上传文件
     * @param ossBucket
     * @param key
     * @param in
     */
    public String upload(String ossBucket, String key, InputStream in){
        return this.upload(ossBucket, key, in, null);
    }
}
