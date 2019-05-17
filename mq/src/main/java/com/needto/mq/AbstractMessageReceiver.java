package com.needto.mq;


import com.alibaba.fastjson.JSON;

/**
 * @author Administrator
 * 实际需要继承的消息接收器
 */
abstract public class AbstractMessageReceiver implements IReceiver {

    @Override
    public void receive(String msg) {
        this.receive(JSON.parseObject(msg, Message.class));
    }

    /**
     * 转换之后的消息数据
     * @param message
     */
    abstract public void receive(Message message);


}
