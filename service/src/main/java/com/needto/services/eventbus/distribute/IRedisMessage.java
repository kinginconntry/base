package com.needto.services.eventbus.distribute;

/**
 * @author Administrator
 * redis消息队列接口
 */
public interface IRedisMessage {

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
