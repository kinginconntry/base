package com.needto.cache.msgbus;

/**
 * @author Administrator
 */
public interface IRedisReceiver<T> {

    void receive(T msg);
}
