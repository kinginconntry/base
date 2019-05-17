package com.needto.mq;

/**
 * @author Administrator
 * 通用消息接收器
 */
public interface IReceiver {

    void receive(String msg);

    String topic();
}
