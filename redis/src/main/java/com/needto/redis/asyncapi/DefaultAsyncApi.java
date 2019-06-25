package com.needto.redis.asyncapi;

import com.needto.redis.entity.AsyncResult;
import com.needto.redis.service.AsyncApiResultService;
import com.needto.tool.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */

@RestController
public class DefaultAsyncApi {

    @Autowired
    private AsyncApiResultService asyncApiResultService;

    /**
     * 获取异步任务结果
     * @param guid
     * @return
     */
    @RequestMapping(value = {"/app/async/task/get", "/sys/async/task/get", "/admin/async/task/get"})
    @ResponseBody
    public Result<AsyncResult> getResult(HttpServletRequest request, @RequestParam String guid){
        return Result.forSuccess(asyncApiResultService.getResult(guid));
    }
}
