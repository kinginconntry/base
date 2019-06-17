package com.needto.commonoss.aliyunoss;

import com.needto.common.entity.Target;
import com.needto.firewall.frequency.FrequencyService;
import com.needto.tool.entity.Dict;
import com.needto.tool.entity.Result;
import com.needto.tool.utils.Assert;
import com.needto.web.context.WebEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * 默认的阿里云oss asyncapi
 */
@RestController
public class AliyunOssApi {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private OssService ossService;

    @Autowired
    private FrequencyService frequencyService;

    @Value("${oss.default.bucket}")
    private String bucket;

    private boolean webFilter(Target target){
        Assert.validateNull(target, "NO_CLIENT", "");
        if(frequencyService == null){
            return false;
        }
        return frequencyService.filter(WebEnv.getiClientCache().getGuid(target), "/web/oss/build", 10, 5, 600);
    }

    /**
     * 获取oss上传参数
     *
     * app 开头的链接需要在项目中定义
     * @return
     */
    @RequestMapping(value = {"/admin/oss/build", "/sys/oss/build"})
    @ResponseBody
    public Result<OssData> getParam(@RequestBody Dict param){
        String dir = param.getValue("dir");
        OssData.CallBack callBack = param.getValue("callback");
        return Result.forSuccessIfNotNull(ossService.getOssParam(bucket, dir, callBack));
    }

    /**
     * web客端端访问限制，必须要有客户标识，有上传频率控制
     * @param dir
     * @return
     */
    @RequestMapping(value = {"/web/oss/build"})
    @ResponseBody
    public Result<OssData> getWebParam(HttpServletRequest httpServletRequest, @RequestParam String dir){
        if(webFilter(WebEnv.getClient(httpServletRequest))){
            return Result.forError("", "系统繁忙");
        }
        return Result.forSuccessIfNotNull(ossService.getOssParam(bucket, dir, null));
    }

    /**
     * 校验oss参数是否过期
     * @param oldExpireTime
     * @return
     */
    @RequestMapping(value = {"/admin/oss/validate", "/sys/oss/validate", "/app/oss/validate", "/web/oss/validate"})
    @ResponseBody
    public Result<Void> validateExpire(@RequestParam Long oldExpireTime){
        if(oldExpireTime == null){
            return Result.forError();
        }
        if(ossService.checkOssExpire(oldExpireTime)){
            return Result.forSuccess();
        }else{
            return Result.forError();
        }
    }

    /**
     * app删除
     * @param urls
     * @return
     */
    @RequestMapping(value = {"/app/oss/delete", "/admin/oss/delete", "/sys/oss/delete"})
    @ResponseBody
    public Result<Void> deleteAppOss(HttpServletRequest httpServletRequest, @RequestBody List<String> urls){
        List<String> deletes = ossService.delete(bucket, urls);
        if(CollectionUtils.isEmpty(deletes)){
            return Result.forError();
        }else{
            applicationContext.publishEvent(new OssDeleteEvent(this, deletes, WebEnv.getClient(httpServletRequest)));
            return Result.forSuccess();
        }
    }

}
