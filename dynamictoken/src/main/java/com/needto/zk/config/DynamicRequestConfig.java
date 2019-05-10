package com.needto.zk.config;

import com.needto.common.data.Constant;
import com.needto.zk.service.DynamicSignService;
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
    private DynamicSignService dynamicSignService;

    @Bean
    public RequestInterceptor headerInterceptor() {
        return template -> template.header(Constant.SIGN_KEY, dynamicSignService.getSign());
    }
}
