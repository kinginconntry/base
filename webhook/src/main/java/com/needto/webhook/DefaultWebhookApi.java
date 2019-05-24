package com.needto.webhook;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * 默认提供的webhook asyncapi
 */
@RestController
public class DefaultWebhookApi {

    @Autowired
    private WebHookService webHookService;

    /**
     * 查找webhook数据
     * @param query
     * @return
     */
    @PostMapping(value = {"/app/webhook/find", "/sys/webhook/find", "/admin/webhook/find"})
    @ResponseBody
    public Result<List<WebHook>> find(@RequestBody Query query){
        return Result.forSuccess(webHookService.find(query, GlobalEnv.getOwner()));
    }

    /**
     * 登录客户删除
     * @param ids
     * @return
     */
    @PostMapping(value = {"/app/webhook/delete", "/sys/webhook/delete", "/admin/webhook/delete"})
    @ResponseBody
    public Result<Long> delete(@RequestBody List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return Result.forError("NO_ID", "");
        }
        return Result.forSuccess(webHookService.removeByIds(ids, GlobalEnv.getOwner()));
    }

    /**
     * 管理员端保存
     * @param webHook
     * @return
     */
    @PostMapping(value = {"/app/webhook/save", "/sys/webhook/save", "/admin/webhook/save"})
    @ResponseBody
    public Result<WebHook> save(@RequestBody WebHook webHook){
        if(webHook == null){
            return Result.forError("NO_DATA", "");
        }
        webHook.setOwner(GlobalEnv.getOwner());
        return Result.forSuccess(webHookService.save(webHook));
    }
}
