package com.needto.web.config;

import com.needto.common.entity.Target;
import com.needto.web.inter.IClientCache;
import com.needto.web.inter.IClientInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 */
@Configuration
public class WebConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public IClientCache iClientCache(){
        IClientCache iClientCache;
        try {
            iClientCache = applicationContext.getBean(IClientCache.class);
        }catch (Exception e){
            iClientCache = applicationContext.getAutowireCapableBeanFactory().createBean(GuavaClientCache.class);
        }
        return iClientCache;
    }

    @Bean
    public IClientInit iClientInit(){
        IClientInit iClientInit;
        try {
            iClientInit = applicationContext.getBean(IClientInit.class);
        }catch (Exception e){
            iClientInit = new IClientInit() {
                @Override
                public void init(Target target, HttpServletRequest httpServletRequest) {

                }
            };
        }
        return iClientInit;
    }
}
