package com.needto.zk.filter;

import com.needto.common.entity.Result;
import com.needto.common.filter.CommonFilter;
import com.needto.common.utils.RequestUtil;
import com.needto.common.utils.ResponseUtil;
import com.needto.zk.service.DynamicSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Administrator
 * 内部应用调用过滤器
 */
@WebFilter(filterName = "serviceFilter", urlPatterns = {"/_/_/*"})
public class DynamicSignFilter extends CommonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicSignFilter.class);

    private DynamicSignService dynamicSignService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(dynamicSignService == null){
            dynamicSignService = this.applicationContext.getBean(DynamicSignService.class);
        }

        String sign = RequestUtil.getInnerServiceSign((HttpServletRequest) request);
        if(StringUtils.isEmpty(sign)){
            ResponseUtil.outJson(response, Result.forError("SIGN_ERROR", "签名错误"));
            return;
        }

        if(!dynamicSignService.isValid(sign)){
            ResponseUtil.outJson(response, Result.forError("INVALID_SIGN", "无效签名"));
            return;
        }
        chain.doFilter(request,response);
    }
}