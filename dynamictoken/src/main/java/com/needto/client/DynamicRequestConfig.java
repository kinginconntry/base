package com.needto.client;

import com.needto.data.Constant;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Administrator
 */
@Configuration
public class DynamicRequestConfig {

    @Resource
    private IDynamicClient iDynamicService;

    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> template.header(Constant.CLIENT_IDENTIFYING, iDynamicService.getClientIde());
    }
}
