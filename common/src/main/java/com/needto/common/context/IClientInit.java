package com.needto.common.context;


import com.needto.common.entity.Target;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
public interface IClientInit {

    /**
     * 检查是否初始化，未初始化然后才进行初始化
     * @param target 客户端标识
     * @param httpServletRequest
     */
    void init(Target target, HttpServletRequest httpServletRequest);
}
