package com.needto.web.inter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * 客户端拦截接口
 */
public interface ClientIntercepter {

    /**
     * 首次登录初始化(若没有初始化则进行初始化操作，若进行过初始化则跳过)
     * @param httpServletRequest
     * @return 若返回false，则需要将失败信息写入到httpServletResponse中
     */
    default void init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){}

    /**
     * 是否已经授权过
     * @param httpServletRequest
     * @return 若返回false，则需要将失败信息写入到httpServletResponse中
     */
    default boolean auth(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){ return true; };
}
