package com.needto.mq;

/**
 * @author Administrator
 * 消息数据接口
 */
public interface IMessage {

    /**
     * 获取分组
     * @return
     */
    String getGroup();

    /**
     * 获取客户端标识
     * @return
     */
    String getClient();
}
