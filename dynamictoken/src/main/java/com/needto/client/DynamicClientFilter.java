package com.needto.client;

import com.needto.data.Constant;
import com.needto.tool.entity.Result;
import com.needto.web.filter.CommonFilter;
import com.needto.web.utils.ResponseUtil;
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
public class DynamicClientFilter extends CommonFilter {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicClientFilter.class);

    private DynamicClient iDynamicService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(iDynamicService == null){
            iDynamicService = this.applicationContext.getBean(DynamicClient.class);
        }

        String clientGuid = ((HttpServletRequest)request).getHeader(Constant.CLIENT_IDENTIFYING);
        if(StringUtils.isEmpty(clientGuid)){
            ResponseUtil.outJson(response, Result.forError("CLIENT_IDE_ERROR", ""));
            return;
        }

        if(!iDynamicService.isValid(clientGuid)){
            ResponseUtil.outJson(response, Result.forError("INVALID_CLIENT_IDE", ""));
            return;
        }
        chain.doFilter(request,response);
    }
}
