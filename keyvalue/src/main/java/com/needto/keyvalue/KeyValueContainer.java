package com.needto.keyvalue;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.common.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class KeyValueContainer {

    @Autowired
    private ApplicationContext applicationContext;
    /**
     * key生成器
     */
    private final Map<String, Map<String, IKeyValueService>> MAP = new HashMap<>();

    /**
     * 授权的keyvalue
     */
    private final Map<String, Map<String, KeyValue>> KEY_VALUE_MAP = new HashMap<>();

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

            if(!KEY_VALUE_MAP.containsKey(group)){
                KEY_VALUE_MAP.put(group, new HashMap<>());
            }
            KEY_VALUE_MAP.get(group).put(keyValue.getKey(), keyValue);

        });
    }

    /**
     * 获取某个分组的所有key
     * @param group
     * @return
     */
    public List<KeyValue> getKeys(String group){
        Assert.validateNull(group);
        Map<String, KeyValue> map = KEY_VALUE_MAP.get(group);
        if(map == null){
            return new ArrayList<>(0);
        }else{
            return Lists.newArrayList(map.values());
        }
    }

    /**
     * 根据分组key获取源数据
     * @param group
     * @param target 归属目标
     * @param sourceList
     * @return
     */
    public Map<String, List<KeyValue>> getKeyValues(String group, Target target, List<String> sourceList){
        Assert.validateNull(group);
        Map<String, List<KeyValue>> map = new HashMap<>();
        if(!CollectionUtils.isEmpty(sourceList)){
            Map<String, IKeyValueService> temp = MAP.get(group);
            if(temp != null){
                for(String key : sourceList){
                    IKeyValueService iKeyValueService = temp.get(key);
                    if(iKeyValueService != null){
                        map.put(key, iKeyValueService.getValue(target));
                    }
                }
            }
        }
        return map;
    }
}
