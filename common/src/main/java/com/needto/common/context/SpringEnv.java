package com.needto.common.context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * spirng容器环境静态类
 * @author
 */
public class SpringEnv {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(SpringEnv.class);

    /**
     * 环境上下文
     */
    private static ApplicationContext applicationContext = null;

    /**
     * 全局配置属性
     */
    private static Environment environment = null;

    /**
     * 获取容器环境上下文
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 设置容器环境上下文
     * @param applicationContext
     */
    public static void setApplicationContext(ApplicationContext applicationContext) {
        SpringEnv.applicationContext = applicationContext;
        if(applicationContext != null){
            setEnvironment(applicationContext.getEnvironment());
        }
    }

    /**
     * 获取环境对象
     * @return
     */
    public static Environment getEnvironment() {
        return environment;
    }

    /**
     * 设置环境对象
     * @param environment
     */
    public static void setEnvironment(Environment environment) {
        SpringEnv.environment = environment;
    }

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

    public static String getAppName(){
        return environment.getProperty("spring.application.name");
    }

    public static String getAppPort(){
        return environment.getProperty("server.port");
    }

    public static boolean isDebug(){
        return "debug".equalsIgnoreCase(environment.getProperty("debug", "false"));
    }

    /**
     * 获取spring配置
     * @param key
     * @return
     */
    public static String getProperty(String key){
        if(environment == null){
            return null;
        }
        return environment.getProperty(key);
    }

    /**
     * 获取spring配置
     * @param key
     * @param defaultVal
     * @return
     */
    public static String getProperty(String key, String defaultVal){
        if(environment == null){
            return null;
        }
        return environment.getProperty(key, defaultVal);
    }
}
