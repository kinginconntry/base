package com.needto.web.filter;

import com.needto.web.context.WebEnv;
import com.needto.web.inter.ClientIntercepter;
import com.sun.javafx.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * 1 设置web环境session
 * 2 保证过滤的客户端能够进行一些通用的过滤和初始化操作（默认为sys,admin,app路径，即通用登录路径）
 */
@Order(ClientFilter.ORDER)
@WebFilter(filterName = "ClientFilter", urlPatterns = {"*"})
public class ClientFilter extends CommonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ClientFilter.class);

    /**
     * 执行顺序，在进行session初始化后执行
     */
    public static final int ORDER = Integer.MAX_VALUE + 100;

    /**
     * 是否已经进行过初始化
     */
    private boolean initFlag = false;

    /**
     * 客户端初始化
     */
    private ClientIntercepter clientIntercepter;

    /**
     *  需要被拦截的url前缀
     */
    private Set<String> prefixSet = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        super.init(filterConfig);
        String clientUrlPrefix = this.applicationContext.getEnvironment().getProperty("client.filter.url.prefix", "");
        if(!StringUtils.isEmpty(clientUrlPrefix)){
            String[] paths = Utils.split(clientUrlPrefix, ",");
            for(int i = 0, len = paths.length; i < len; i++){
                String path = paths[i];
                if(!StringUtils.isEmpty(path)){
                    prefixSet.add(path);
                }
            }
        }else{
            // 默认路径
            prefixSet.add("/app");
            prefixSet.add("/admin");
            prefixSet.add("/sys");
        }
    }

    private boolean prefixStart(String url){
        for(String prefix : prefixSet){
            if(url.startsWith(prefix)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            HttpSession httpSession = httpServletRequest.getSession();
            WebEnv.setHttpSession(httpSession);
            if(!prefixStart(httpServletRequest.getRequestURI())){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }

            // 初始化拦截器
            if(!initFlag){
                this.clientIntercepter = this.applicationContext.getBean(ClientIntercepter.class);
                initFlag = true;
                LOG.debug("客户端过滤器初始化，clientIntercepter {}", clientIntercepter);
            }

            // 这里需要判断是否已经初始化
            if(WebEnv.isInit()){
                Boolean flag = clientIntercepter.init(httpServletRequest, httpServletResponse);
                if(flag != null && !flag){
                    return;
                }
                WebEnv.setGuid(clientIntercepter.guid(httpServletRequest, httpServletResponse));
                WebEnv.setInit();
            }else{
                Boolean flag = clientIntercepter.filter(httpServletRequest, httpServletResponse);
                if(flag != null && !flag){
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        } finally {
            // 移除当前线程可客户端信息的绑定
            WebEnv.removeHttpSession();
        }

    }
}
