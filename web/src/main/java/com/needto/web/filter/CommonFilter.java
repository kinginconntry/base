package com.needto.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    protected final static Logger LOG = LoggerFactory.getLogger(CommonFilter.class);

    protected ApplicationContext applicationContext;

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.debug("初始化过滤器");
        ServletContext context = filterConfig.getServletContext();
        applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
    }

    @Override
    public void destroy() {

    }
}
