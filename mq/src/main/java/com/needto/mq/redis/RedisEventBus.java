package com.needto.mq.redis;

import com.needto.common.utils.Assert;
import com.needto.mq.IMessage;
import com.needto.mq.IReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于redis的事件通知bus
 * @author Administrator
 */
@Service
public class RedisEventBus {

    private static final Logger LOG = LoggerFactory.getLogger(RedisEventBus.class);

    private static final Map<String, List<IReceiver>> RECEIVE_MAP = new HashMap<>();

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, IReceiver> iRedisReceiverMap = applicationContext.getBeansOfType(IReceiver.class);
        for(IReceiver iRedisReceiver : iRedisReceiverMap.values()){
            addTopic(iRedisReceiver);
        }
    }

    /**
     * 给单个topic发送消息
     * @param channel
     * @param msg
     */
    public void sendMsg(String channel, IMessage msg){
        redisTemplate.convertAndSend(channel, msg);
    }

    /**
     * 给所有topic发送消息
     * @param msg
     */
    public void broadcastMessage(IMessage msg){
        for(String topic : RECEIVE_MAP.keySet()){
            redisTemplate.convertAndSend(topic, msg);
        }
    }

    /**
     *
     * @param iRedisReceiver
     */
    public void addTopic(IReceiver iRedisReceiver) {
        Assert.validateNull(iRedisReceiver);
        String topic = iRedisReceiver.topic();
        Assert.validateStringEmpty(topic);
       if(!RECEIVE_MAP.containsKey(topic)){
           RECEIVE_MAP.put(topic, new ArrayList<>());
       }
       for(IReceiver temp : RECEIVE_MAP.get(topic)){
           if(temp.getClass().equals(iRedisReceiver.getClass())){
               return;
           }
       }
        RECEIVE_MAP.get(topic).add(iRedisReceiver);
    }

    /**
     * 接收消息
     * @param topic
     * @param msg
     */
    public void receive(String topic, String msg){
        List<IReceiver> redisReceivers = RECEIVE_MAP.get(topic);
        if(!CollectionUtils.isEmpty(redisReceivers)){
            for(IReceiver iRedisReceiver : redisReceivers){
                iRedisReceiver.receive(msg);
            }
        }
    }

}
