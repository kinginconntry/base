package com.needto.services.eventbus.distribute;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author Administrator
 * redis 消息队列注册器
 */
@Configuration
public class RedisMessageConfigure {

    @Resource
    private RedisEventBus redisEventBus;

    @Bean
    @SuppressWarnings("all")
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, ApplicationContext appContextHelper) throws Exception {
        Map<String, Object> listenerAdapters = appContextHelper.getBeansWithAnnotation(RedisMessageListenerAdapterRegister.class);
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        if (!CollectionUtils.isEmpty(listenerAdapters)) {
            for (Map.Entry<String, Object> entry : listenerAdapters.entrySet()) {
                Object v = entry.getValue();
                if (v instanceof MessageListenerAdapter) {
                    MessageListenerAdapter listenerAdapter = (MessageListenerAdapter) v;
                    String topic = v.getClass().getAnnotation(RedisMessageListenerAdapterRegister.class).topic();
                    redisEventBus.addTopic(topic);
                    container.addMessageListener(listenerAdapter, new PatternTopic(topic));
                }
            }
        }
        return container;
    }
}
