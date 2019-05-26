package com.needto.mq;

import com.needto.common.context.SpringEnv;
import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * 通用消息
 */
public class Message implements IMessage {

    /**
     * 消息分组
     */
    public String group;

    /**
     * 客户端
     */
    public String client;

    /**
     * 消息数据
     */
    public Dict data;

    public Message() {
        this.client = SpringEnv.getProperty("spring.application.name");
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

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }
}
