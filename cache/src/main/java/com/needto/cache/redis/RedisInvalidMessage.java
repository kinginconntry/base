package com.needto.cache.redis;

import java.util.List;

/**
 * @author Administrator
 * redis值失效消息类
 */
public class RedisInvalidMessage {

    /**
     * 数据发生变化时，对应的key
     */
    private List<String> keys;

    public RedisInvalidMessage(List<String> keys) {
        this.keys = keys;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }
}
