package com.needto.common.utils;

import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Env {

    /**
     * 获取spring带有共同前缀的配置
     * @param environment
     * @param commonPrefix
     * @return
     */
    public static Map<String, Map<String, Object>> getProperties(AbstractEnvironment environment, String commonPrefix){
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Map<String, Object>> map = new HashMap<>();
        propertySources.forEach(propertySource -> {
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mps = (MapPropertySource) propertySource;
                Set<String> keys = mps.getSource().keySet();
                for (String key : keys) {
                    if (key.startsWith(commonPrefix)) {
                        String emailtemp = key.replace(commonPrefix, "");
                        int index = emailtemp.indexOf(".");
                        String prefix = "";
                        if(index > -1 && index < (emailtemp.length() - 1)){
                            prefix = emailtemp.substring(0, index);
                        }
                        if(!StringUtils.isEmpty(prefix)){
                            if(!map.containsKey(prefix)){
                                map.put(prefix, new HashMap<>());
                            }
                            String temp = emailtemp.replace(prefix + ".", "");
                            map.get(prefix).put(temp, mps.getProperty(key));
                        }
                    }
                }
            }
        });
        return map;
    }
}
