package com.needto.common.context;

import com.needto.common.data.Constant;
import com.needto.common.entity.Dict;
import com.needto.common.entity.Target;
import com.needto.common.inter.IClientCache;
import com.needto.common.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * 全局环境静态方法，只可使用在spirng容器完全启动之后的业务中，不可在@PostConstruct中使用，因为该注解在静态方法初始化之前调用
 */
public class GlobalEnv {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(GlobalEnv.class);

    /**
     * 登录用户的主用户id
     */
    public static final String OWNER_KEY = "__owner";

    /**
     * 当前线程的客户端信息
     */
    private static final ThreadLocal<Dict> CURRENT_CLIENT_CACHE = new ThreadLocal<>();

    /**
     * 环境上下文
     */
    private static ApplicationContext applicationContext = null;

    /**
     * 全局配置属性
     */
    private static Environment environment = null;

    /**
     * 客户端缓存
     */
    private static IClientCache iClientCache;

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
        GlobalEnv.applicationContext = applicationContext;
    }

    public static void setClientCache(Dict dict){
        if(dict != null){
            CURRENT_CLIENT_CACHE.set(dict);
        }
    }

    public static void removeClientCache(){
        CURRENT_CLIENT_CACHE.remove();
    }

    public static Dict getClientCache(){
        return CURRENT_CLIENT_CACHE.get();
    }

    /**
     * 获取客户端缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getClientData(String key){
        Assert.validateNull(key);
        Dict dict = getClientCache();
        if(dict == null){
            return null;
        }
        return dict.getValue(key);
    }

    public static String getOwner(){
        return getClientData(OWNER_KEY);
    }

    public static Target getOwnerTarget(){
        String owner = getOwner();
        Target target = new Target();
        target.setGuid(owner);
        target.setType(OWNER_KEY);
        return target;
    }

    public static void setOwner(String key){
        Dict dict = getClientCache();
        if(dict != null){
            dict.put(OWNER_KEY, key);
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
        GlobalEnv.environment = environment;
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

    public static IClientCache getiClientCache() {
        return GlobalEnv.iClientCache;
    }

    public static void setiClientCache(IClientCache iClientCache) {
        GlobalEnv.iClientCache = iClientCache;
    }

    /**
     * 获取客户端标识信息
     * @param httpServletRequest
     * @return
     */
    public static Target getClient(HttpServletRequest httpServletRequest){
        return (Target) httpServletRequest.getAttribute(Constant.FINGER_KEY);
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
}
