package com.needto.web.context;

import com.needto.common.entity.Target;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 * 全局环境静态方法，只可使用在spirng容器完全启动之后的业务中，不可在@PostConstruct中使用，因为该注解在静态方法初始化之前调用
 */
public class WebEnv {

    /**
     * 日志
     */
    private static final Logger LOG = LoggerFactory.getLogger(WebEnv.class);

    /**
     * 客户端已经进行初始化的标识（true， false， null）
     */
    public static final String CLIENT_INIT = "_CI";

    /**
     * 登录主用户id
     * owner转化成Target 时的类型
     */
    public static final String OWNER_TYPE = "_OWNER";

    /**
     * 登录用户唯一key
     */
    public static final String OWNER_KEY = "_WNK";

    /**
     * guid转化成Target 时的类型
     */
    public static final String GUID_TYPE = "_GUID";

    /**
     * 客户端唯一key，在分布式环境下通用
     */
    public static final String GUID_KEY = "_GDK";

    /**
     * 当前线程的客户端信息
     */
    private static final ThreadLocal<HttpSession> CURRENT_CLIENT_CACHE = new ThreadLocal<>();


    public static void setHttpSession(HttpSession httpSession){
        if(httpSession != null){
            CURRENT_CLIENT_CACHE.set(httpSession);
        }
    }

    public static void removeHttpSession(){
        CURRENT_CLIENT_CACHE.remove();
    }

    public static HttpSession getHttpSession(){
        return CURRENT_CLIENT_CACHE.get();
    }

    /**
     * 获取客户端缓存数据
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getSessionData(String key, T defaultVal){
        HttpSession httpSession = getHttpSession();
        T v =  (T) httpSession.getAttribute(key);
        if(v == null){
            return defaultVal;
        }else{
            return v;
        }
    }

    public static <T> T getSessionData(String key){
        return getSessionData(key, null);
    }

    public static void setSessionData(String key, Object o){
        HttpSession httpSession = getHttpSession();
        httpSession.setAttribute(key, o);
    }

    public static void setOwner(String key){
        setSessionData(OWNER_KEY, key);
    }

    public static String getOwner(){
        return getSessionData(OWNER_KEY);
    }

    public static Target getOwnerTarget(){
        return new Target(OWNER_TYPE, getOwner());
    }

    public static void setGuid(String key){
        setSessionData(GUID_KEY, key);
    }

    public static String getGuid(){
        return getSessionData(GUID_KEY);
    }

    public static Target getGuidTarget(){
        return new Target(GUID_TYPE, getGuid());
    }

    /**
     * 初始化客户端
     */
    public static void setInit(){
        setSessionData(CLIENT_INIT, true);
    }

    /**
     * 客户端是否已经初始化
     * @return
     */
    public static boolean isInit(){
        return getSessionData(CLIENT_INIT, false);
    }
}
