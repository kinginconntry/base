package com.needto.services.keyvalue;

import com.google.common.collect.Lists;
import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Result;
import com.needto.common.entity.Target;
import com.needto.common.services.keyvalue.IKeyValueService;
import com.needto.common.services.keyvalue.KeyValue;
import com.needto.common.utils.RequestUtil;
import com.needto.common.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * keyvalue 控制器
 */
@RestController
public class DefaultKeyValueApi {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * key生成器
     */
    private final Map<String, com.needto.common.services.keyvalue.IKeyValueService> MAP = new HashMap<>();

    /**
     * 授权的keyvalue（包含非授权的）
     */
    private final Map<String, com.needto.common.services.keyvalue.KeyValue> AUTH_KEY_VALUE_MAP = new HashMap<>();
    /**
     * 非授权的keyvalue
     */
    private final Map<String, com.needto.common.services.keyvalue.KeyValue> NO_AUTH_KEY_VALUE_MAP = new HashMap<>();

    @PostConstruct
    public void init(){
        Map<String, com.needto.common.services.keyvalue.IKeyValueService> temp = applicationContext.getBeansOfType(com.needto.common.services.keyvalue.IKeyValueService.class);
        temp.forEach((k, v) -> {
            com.needto.common.services.keyvalue.KeyValue keyValue = v.getName();
            MAP.put(keyValue.getKey(), v);
            if(!v.isAuth()){
                NO_AUTH_KEY_VALUE_MAP.put(keyValue.getKey(), keyValue);
            }
            AUTH_KEY_VALUE_MAP.put(keyValue.getKey(), keyValue);

        });
    }

    public List<com.needto.common.services.keyvalue.KeyValue> getKeys(boolean isAuth){
        return Lists.newArrayList(isAuth ? AUTH_KEY_VALUE_MAP.values() : NO_AUTH_KEY_VALUE_MAP.values());
    }

    public Map<String, List<com.needto.common.services.keyvalue.KeyValue>> getKeyValues(Target target, List<String> sourceList, boolean isAuth){
        Map<String, List<com.needto.common.services.keyvalue.KeyValue>> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(sourceList)){
            if(isAuth){
                for(String key : sourceList){
                    com.needto.common.services.keyvalue.IKeyValueService iKeyValueService = MAP.get(key);
                    if(iKeyValueService != null){
                        map.put(key, iKeyValueService.getValue(target));
                    }
                }
            }else{
                for(String key : sourceList){
                    IKeyValueService iKeyValueService = MAP.get(key);
                    if(iKeyValueService != null){
                        if(iKeyValueService.isAuth()){
                            continue;
                        }
                        map.put(key, iKeyValueService.getValue(target));
                    }
                }
            }
        }
        return map;
    }


    /**
     * 获取keyvalue keys api
     * @param request
     * @return
     */
    @RequestMapping(value = {"/app/keyvalue/keys", "/admin/keyvalue/keys", "/sys/keyvalue/keys", "/web/keyvalue/keys"})
    @ResponseBody
    public Result<List<com.needto.common.services.keyvalue.KeyValue>> getKeys(HttpServletRequest request){
        if(RequestUtil.urlType(request.getRequestURI(), "/web")){
            return Result.forSuccess(getKeys(false));
        }else{
            return Result.forSuccess(getKeys(true));
        }
    }

    /**
     * 获取keyvalue values api
     * @param request
     * @param sources
     * @return
     */
    @RequestMapping(value = {"/app/keyvalue/values", "/admin/keyvalue/values", "/sys/keyvalue/values", "/web/keyvalue/values"})
    @ResponseBody
    public Result<Map<String, List<KeyValue>>> getKeyValues(HttpServletRequest request, @RequestParam String sources){
        if(RequestUtil.urlType(request.getRequestURI(), "/web")){
            return Result.forSuccess(getKeyValues(GlobalEnv.getClient(request), Utils.getList(sources), false));
        }else{
            return Result.forSuccess(getKeyValues(GlobalEnv.getClient(request), Utils.getList(sources), true));
        }
    }


}
