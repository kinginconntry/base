package com.needto.mq.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;


/**
 * @author Administrator
 * redis 消息队列注册器
 */
@Configuration
public class RedisMessageConfigure {

    @Bean
    @SuppressWarnings("all")
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) throws Exception {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        return container;
    }
}
