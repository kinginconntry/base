package com.needto.web.inter;


import com.needto.tool.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * 客户端拦截接口
 */
public interface ClientIntercepter {

    /**
     * 首次登录初始化
     * @param httpServletRequest
     * @return 若返回false，则需要将失败信息写入到httpServletResponse中
     */
    default Boolean init(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){ return null; }

    /**
     * 初始化成功后会产生一个唯一id，该id在分布式环境下也可使用，用来表示同一个用户
     * @return
     */
    default String guid(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){ return Utils.getGuid(); }

    /**
     * 除去首次登录，其他每次访问时进行的拦截
     * @param httpServletRequest
     * @return 若返回false，则需要将失败信息写入到httpServletResponse中
     */
    default Boolean filter(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){ return null; }
}
