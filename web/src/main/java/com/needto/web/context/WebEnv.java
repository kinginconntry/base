package com.needto.web.context;

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

    public static void removeSessionData(String key){
        HttpSession httpSession = getHttpSession();
        httpSession.removeAttribute(key);
    }

}
