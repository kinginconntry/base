package com.needto.commonoss.aliyunoss;

import com.needto.tool.entity.Dict;
import com.needto.tool.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * @author Administrator
 * 默认的阿里云oss asyncapi
 */
@RestController
public class AliyunOssApi {

    @Autowired
    private OssService ossService;


    @Value("${oss.default.bucket}")
    private String bucket;

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

}
