package com.needto.mq;

/**
 * @author Administrator
 * 通用消息接收器
 */
public interface IReceiver {

    /**
     * 接收消息
     * @param msg
     */
    void receive(String msg);

    /**
     * 主题
     * @return
     */
    String topic();
}
