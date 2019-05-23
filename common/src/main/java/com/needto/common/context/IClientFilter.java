package com.needto.common.context;

import com.needto.common.data.Constant;
import com.needto.common.entity.Target;
import com.needto.common.filter.CommonFilter;
import com.needto.common.inter.IClientCache;
import com.needto.common.inter.IClientInit;
import com.needto.common.utils.ResponseUtil;
import com.sun.javafx.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * 保证每一个客户端都有一个唯一的指纹信息
 */
@Order(1)
@WebFilter(filterName = "IClientFilter", urlPatterns = {"*"})
public class IClientFilter extends CommonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(IClientFilter.class);

    /**
     * 是否已经进行过初始化
     */
    private boolean initFlag = false;

    /**
     * 客户端初始化
     */
    private IClientInit iClientInit;

    /**
     * 客户端缓存
     */
    private IClientCache iClientCache;

    /**
     *  需要被拦截的url前缀
     */
    private Set<String> prefixSet = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) {
        super.init(filterConfig);
        String iclientUrlPrefix = this.applicationContext.getEnvironment().getProperty("iclient.url.prefix", "");
        prefixSet.add("/app");
        prefixSet.add("/admin");
        prefixSet.add("/sys");
        if(!StringUtils.isEmpty(iclientUrlPrefix)){
            String[] paths = Utils.split(iclientUrlPrefix, ",");
            for(int i = 0, len = paths.length; i < len; i++){
                String path = paths[i];
                if(!StringUtils.isEmpty(path)){
                    prefixSet.add(path);
                }
            }
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain){
        try{
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            if(!prefixStart(httpServletRequest.getRequestURI())){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            if(!initFlag){
                Environment environment = this.applicationContext.getEnvironment();
                try{
                    this.iClientInit = (IClientInit) this.applicationContext.getBean(environment.getProperty("iclient.cache", "guavaClientCache"));
                }catch (Exception e){
                    this.iClientInit = (target, httpServletRequest1) -> {};
                }
                this.iClientCache = this.applicationContext.getBean(IClientCache.class);
                GlobalEnv.setiClientCache(iClientCache);
                initFlag = true;
                LOG.debug("客户端过滤器初始化，iClientCache {}，iClientInit {}", iClientCache, iClientInit);
            }

            // 刷新客户端缓存
            Target client = iClientCache.refresh(httpServletRequest);
            // 将客户端信息与当前线程绑定
            GlobalEnv.setClientCache(iClientCache.get(client));
            // 这里需要判断是否已经初始化
            iClientInit.init(client, httpServletRequest);
            // 设置请求
            httpServletRequest.setAttribute(Constant.FINGER_KEY, client);
            // 设置响应
            ResponseUtil.setFingerPrint(servletResponse, client);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        } finally {
            // 移除当前线程可客户端信息的绑定
            GlobalEnv.removeClientCache();
        }

    }
}
