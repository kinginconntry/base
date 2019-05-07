package com.needto.common.entity;

import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author Administrator
 */
public class Dict extends HashMap {
    public <T> T getValue(String key, T defaultValue){
        if(StringUtils.isEmpty(key)){
            return defaultValue;
        }
        Object o = this.get(key);
        if(o == null){
            return defaultValue;
        }
        return (T) o;
    }

    public <T> T getValue(String key){
        return this.getValue(key, null);
    }
}
