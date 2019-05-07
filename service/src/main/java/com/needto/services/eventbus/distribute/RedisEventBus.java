package com.needto.services.eventbus.distribute;

import com.needto.common.services.eventbus.distribute.IRedisMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于redis的事件通知bus
 * @author Administrator
 */
@Service
public class RedisEventBus {

    private static final Logger LOG = LoggerFactory.getLogger(RedisEventBus.class);

    @Autowired
    private RedisTemplate redisTemplate;

    private static List<String> topics = new ArrayList<>();

    /**
     * 给单个topic发送消息
     * @param channel
     * @param msg
     */
    public void sendMsg(String channel, com.needto.common.services.eventbus.distribute.IRedisMessage msg){
        redisTemplate.convertAndSend(channel, msg);
    }

    /**
     * 给所有topic发送消息
     * @param msg
     */
    public void broadcastMessage(IRedisMessage msg){
        for(String topic : topics){
            redisTemplate.convertAndSend(topic, msg);
        }
    }

    /**
     *
     * @param topic
     * @throws Exception
     */
    public void addTopic(String topic) throws Exception {
        if(StringUtils.isEmpty(topic)){
            return;
        }
        if(topics.contains(topic)){
            throw new Exception(topic + "存在多个topic");
        }
        topics.add(topic);
    }

}
