package com.needto.keyvalue;

import com.needto.tool.entity.Dict;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class KeyValueService {

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * key生成器
     */
    private final Map<String, Map<String, IKeyValueService>> MAP = new HashMap<>();

    @PostConstruct
    public void init(){
        Map<String, IKeyValueService> temp = applicationContext.getBeansOfType(IKeyValueService.class);
        temp.forEach((k, v) -> {
            KeyValue keyValue = v.getName();
            String group = v.getGroup();
            Assert.validateNull(group);
            if(!MAP.containsKey(group)){
                MAP.put(group, new HashMap<>());
            }
            MAP.get(group).put(keyValue.getKey(), v);
        });
    }


    /**
     * 根据分组key获取源数据
     * @param group
     * @param sourceList
     * @return
     */
    public Map<String, List<KeyValue>> getKeyValues(String group, List<String> sourceList, Dict condition){
        Map<String, List<KeyValue>> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(sourceList)){
            Map<String, IKeyValueService> temp = MAP.get(group);
            if(temp != null){
                for(String key : sourceList){
                    IKeyValueService iKeyValueService = temp.get(key);
                    if(iKeyValueService != null){
                        map.put(key, iKeyValueService.getValue(condition));
                    }
                }
            }
        }
        return map;
    }
}
