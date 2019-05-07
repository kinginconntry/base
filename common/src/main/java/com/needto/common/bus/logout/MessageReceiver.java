package com.needto.common.bus.logout;

import com.needto.common.context.RemoveClientFingerEvent;
import com.needto.common.services.eventbus.distribute.IRedisReceiver;
import com.needto.common.services.eventbus.distribute.RedisMessageListenerAdapterRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@RedisMessageListenerAdapterRegister(topic = "logout")
@Component
public class MessageReceiver implements IRedisReceiver<Message> {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void receive(Message msg) {
        applicationContext.publishEvent(new RemoveClientFingerEvent(this, msg.getTarget()));
    }
}
