package com.needto.cipher.config;

import com.needto.cipher.service.ApiAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class ApiAccessConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ApiAccessService apiAccessService(){
        ApiAccessService apiAccessService;
        try {
            apiAccessService = applicationContext.getBean(ApiAccessService.class);
        }catch (Exception e){
            apiAccessService = applicationContext.getAutowireCapableBeanFactory().createBean(ApiAccessService.class);
        }
        return apiAccessService;
    }
}
