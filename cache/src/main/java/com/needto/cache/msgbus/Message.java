package com.needto.cache.msgbus;

import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Target;

/**
 * @author Administrator
 */
public class Message implements IRedisMessage {

    public String group;

    public String client;

    public Target target;

    public Message() {
        this.client = GlobalEnv.getProperty("spring.application.name");
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public String getClient() {
        return client;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
