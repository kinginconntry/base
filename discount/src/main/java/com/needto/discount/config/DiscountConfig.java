package com.needto.discount.config;

import com.needto.discount.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Administrator
 */
@Configuration
public class DiscountConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public DiscountService discountService(){
        DiscountService discountService;
        try {
            discountService = applicationContext.getBean(DiscountService.class);
        }catch (Exception e){
            discountService = applicationContext.getAutowireCapableBeanFactory().createBean(DiscountService.class);
        }
        return discountService;
    }
}
