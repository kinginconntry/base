package com.needto.config;

import com.needto.client.DynamicClient;
import com.needto.client.ZkDynamicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class DynamicTokenConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DynamicClient dynamicClient(){
        DynamicClient dynamicClient;
        try {
            dynamicClient = applicationContext.getBean(DynamicClient.class);
        }catch (Exception e){
            dynamicClient = applicationContext.getAutowireCapableBeanFactory().createBean(ZkDynamicClient.class);
        }
        return dynamicClient;
    }
}
