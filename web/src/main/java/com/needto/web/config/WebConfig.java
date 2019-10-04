package com.needto.web.config;

import com.needto.web.inter.ClientIntercepter;
import com.needto.web.session.HeaderCookieHttpSessionIdResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author Administrator
 */
@Configuration
public class WebConfig {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    /**
     * 客户端初始化拦截器
     * @return
     */
    @Bean
    public ClientIntercepter clientIntercepter(){
        ClientIntercepter clientIntercepter;
        try {
            clientIntercepter = applicationContext.getBean(ClientIntercepter.class);
        }catch (Exception e){
            clientIntercepter = new ClientIntercepter() {};
        }
        return clientIntercepter;
    }

    /**
     * session 策略
     * @return
     */
    @Bean
    public HeaderCookieHttpSessionIdResolver headerCookieHttpSessionIdResolver(){
        String clientKey = environment.getProperty("client.key", HeaderCookieHttpSessionIdResolver.TOKEN_KEY);
        return new HeaderCookieHttpSessionIdResolver(clientKey);
    }

    /**
     * json转换器
     * @return
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        return new HttpMessageConverters((HttpMessageConverter<?>) MessageConvert.getHttpMessageConverters());
    }
}
