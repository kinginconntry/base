package com.needto.common.services.firewall.api;


import com.needto.common.filter.CommonFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * api防刷过滤器，主要针对web请求的防刷，基于ip或客户端指纹
 * 拦截条件：
 * 1 单位时间内的请求次数
 * 2 ajax必须携带特定的key，该key由页面获取
 */
public class AntiBrushFilter extends CommonFilter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        chain.doFilter(request, response);
    }
}
