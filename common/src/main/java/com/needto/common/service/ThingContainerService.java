package com.needto.common.service;

import com.needto.tool.inter.Thing;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Administrator
 * 事物容器接口
 */
abstract public class ThingContainerService<T extends Thing> {

    protected Map<String, T> map;

    protected Map<String, String> descMap;

    protected Map<String, String> nameMap;

    @Autowired
    protected ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        map = new HashMap<>();
        descMap = new HashMap<>();
        nameMap = new HashMap<>();
        Map<String, T> filterMap = applicationContext.getBeansOfType(getThingClass());
        filterMap.forEach((k, v) -> set(v.getCode(), v, v.getName(), v.getDesc()));
    }

    abstract protected Class<T> getThingClass();

    public boolean contain(String code){
        if(StringUtils.isEmpty(code)){
            return false;
        }
        return map.containsKey(code);
    }

    public void set(String code, T deal, String name, String desc){
        if(StringUtils.isEmpty(code)){
            return;
        }
        Assert.validateCondition(map.containsKey(code), "code repeat");
        map.put(code, deal);
        nameMap.put(code, name);
        descMap.put(code, desc);
    }

    public T get(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return map.get(code);
    }

    public String getName(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return nameMap.get(code);
    }

    public String getDesc(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return descMap.get(code);
    }

    public Collection<String> names(){
        return nameMap.values();
    }

    public Collection<String> descs(){
        return descMap.values();
    }

    public Collection<String> codes(){
        return map.keySet();
    }

}
