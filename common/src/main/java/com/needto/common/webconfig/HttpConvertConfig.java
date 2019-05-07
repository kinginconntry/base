package com.needto.common.webconfig;

import com.needto.common.context.MessageConvert;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * @author Administrator
 * 使用fastjson转换器
 */
@Configuration
public class HttpConvertConfig {

    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        return new HttpMessageConverters((HttpMessageConverter<?>) MessageConvert.getHttpMessageConverters());
    }
}
