package com.needto.mq.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 * 通用字符串消息处理器
 */
@Component
public class StringMessageListener implements MessageListener {

    @Autowired
    private Environment environment;

    @Autowired
    private RedisEventBus redisEventBus;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @PostConstruct
    public void init(){
        String topic = environment.getProperty("redis.message.topic", "*");
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic(topic));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), "utf-8");
            String channel = new String(message.getChannel(), "utf-8");
            redisEventBus.receive(channel, body);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
