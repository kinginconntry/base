package com.needto.keyvalue;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Result;
import com.needto.common.utils.RequestUtil;
import com.needto.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * keyvalue 控制器
 */
@RestController
public class KeyValueApi {

    @Autowired
    private KeyValueContainer keyValueContainer;

    /**
     * 获取keyvalue keys asyncapi
     * @param request
     * @return
     */
    @RequestMapping(value = {"/app/keyvalue/keys", "/admin/keyvalue/keys", "/sys/keyvalue/keys"})
    @ResponseBody
    public Result<List<KeyValue>> getKeys(HttpServletRequest request){
        String group = null;
        if(RequestUtil.urlType(request.getRequestURI(), "/app")){
            group = "app";
        }else if(RequestUtil.urlType(request.getRequestURI(), "/admin")){
            group = "admin";
        }else if(RequestUtil.urlType(request.getRequestURI(), "/sys")){
            group = "sys";
        }
        return Result.forSuccess(keyValueContainer.getKeys(group));
    }

    /**
     * 获取keyvalue values asyncapi
     * @param request
     * @param sources
     * @return
     */
    @RequestMapping(value = {"/app/keyvalue/values", "/admin/keyvalue/values", "/sys/keyvalue/values"})
    @ResponseBody
    public Result<Map<String, List<KeyValue>>> getKeyValues(HttpServletRequest request, @RequestParam String sources){
        String group = null;
        if(RequestUtil.urlType(request.getRequestURI(), "/app")){
            group = "app";
        }else if(RequestUtil.urlType(request.getRequestURI(), "/admin")){
            group = "admin";
        }else if(RequestUtil.urlType(request.getRequestURI(), "/sys")){
            group = "sys";
        }

        return Result.forSuccess(keyValueContainer.getKeyValues(group, GlobalEnv.getClient(request), Utils.getList(sources)));
    }


}
