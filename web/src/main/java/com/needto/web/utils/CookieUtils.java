package com.needto.web.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie读取、设置、删除工具
 */
public class CookieUtils {
    /**
     * 创建cookie
     * @param name 名称
     * @param value 值
     * @param path 路径
     * @param maxAge 存活期。为0则相当于删除；为小于0的值则仅存在于会话；大于0则可能在客户端持久。单位为秒
     * @return
     */
    public static Cookie createCookie(String name, String value, String path, int maxAge) {
        Cookie c = new Cookie(name, value);
        if(!StringUtils.isEmpty(path)) {
            c.setPath(path);
        }
        if(maxAge >= 0) {
            c.setMaxAge(maxAge);
            c.setValue(maxAge == 0 ? null : value);
        }
        return c;
    }

    /**
     * 创建cookie
     * @param name 名称
     * @param value 值
     * @param path 路径
     * @param maxAge 存活期。为0则相当于删除；为小于0的值则仅存在于会话；大于0则可能在客户端持久。单位为秒
     * @return
     */
    public static Cookie createCookie(String name, String value, String path, int maxAge, boolean httpOnly) {
        Cookie c = createCookie(name, value, path, maxAge);
        c.setHttpOnly(httpOnly);
        return c;
    }

    /**
     * Retrieve the first cookie with the given name. Note that multiple
     * cookies can have the same name but different paths or domains.
     *
     * @param request current servlet request
     * @param name    cookie name
     * @return the first cookie with the given name, or {@code null} if none is found
     */
    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equalsIgnoreCase(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
        Cookie c = getCookie(request, name);
        if(c != null) {
            return c.getValue();
        }
        return null;
    }

    /**
     * 将cookie封装到Map里面
     * @param request
     * @return
     */
    public static Map<String,Cookie> readCookieMap(HttpServletRequest request){
        Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
        Cookie[] cookies = request.getCookies();
        if(null!=cookies){
            for(Cookie cookie : cookies){
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
