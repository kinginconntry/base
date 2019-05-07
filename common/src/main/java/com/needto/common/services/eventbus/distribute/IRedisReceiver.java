package com.needto.common.services.eventbus.distribute;

/**
 * @author Administrator
 */
public interface IRedisReceiver<T> {

    void receive(T msg);
}
