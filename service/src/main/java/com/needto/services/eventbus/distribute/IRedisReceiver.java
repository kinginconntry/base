package com.needto.services.eventbus.distribute;

/**
 * @author Administrator
 */
public interface IRedisReceiver<T> {

    void receive(T msg);
}
