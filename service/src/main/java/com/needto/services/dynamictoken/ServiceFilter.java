package com.needto.services.dynamictoken;

import com.needto.common.entity.Result;
import com.needto.common.filter.CommonFilter;
import com.needto.common.utils.RequestUtil;
import com.needto.common.utils.ResponseUtil;
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
@WebFilter(filterName = "ServiceFilter", urlPatterns = {"/_/_/*"})
public class ServiceFilter extends CommonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceFilter.class);

    private DynamicSignService dynamicSignService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(dynamicSignService == null){
            dynamicSignService = this.appContextHelper.getBean(DynamicSignService.class);
        }

        String sign = RequestUtil.getInnerServiceSign((HttpServletRequest) request);
        if(StringUtils.isEmpty(sign)){
            ResponseUtil.outJson(response, Result.forError("", "签名错误"));
            return;
        }

        if(!dynamicSignService.isValid(sign)){
            ResponseUtil.outJson(response, Result.forError("", "无效签名"));
            return;
        }
        chain.doFilter(request,response);
    }
}
