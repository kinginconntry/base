package com.needto.common.filter;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;

/**
 * 带有上下文环境的filter
 * @author Administrator
 */
abstract public class CommonFilter implements Filter {

    protected ApplicationContext appContextHelper;

    @Override
    public void init(FilterConfig filterConfig) {
        System.out.println("初始化过滤器");
        ServletContext context = filterConfig.getServletContext();
        appContextHelper = WebApplicationContextUtils.getWebApplicationContext(context);
    }

    @Override
    public void destroy() {

    }
}
