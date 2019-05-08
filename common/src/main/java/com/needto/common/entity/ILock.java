package com.needto.common.entity;

/**
 * @author Administrator
 * 自定义锁
 */
public interface ILock {

    /**
     * 默认过期时间
     * @return
     */
    default long getDefaultExpire(){
        return 1000L;
    }

    /**
     * 默认的尝试次数
     * @return
     */
    default int getDefaultCount(){
        return 1;
    }

    /**
     * 等待睡眠时间：毫秒
     * @return
     */
    default long getSleep(){
        return 100L;
    }

    /**
     * 尝试获取锁，获取成功，则返回true，获取失败，则放回false
     * @param key 被锁的key
     * @param expire 锁的过期时间，毫秒，等于0则表示没有过期时间
     * @param count 尝试次数，0表示一直尝试，大于0可快速失败
     * @return
     */
    boolean tryLock(String key, long expire, long sleep, int count);

    default boolean tryLock(String key, long expire, int count){
        return tryLock(key, expire, getSleep(), count);
    }

    /**
     * 默认尝试
     * @param key
     * @param expire
     * @return
     */
    default boolean tryLock(String key, long expire){
        return tryLock(key, expire, getSleep(), getDefaultCount());
    }

    /**
     * 默认尝试
     * @param key
     * @return
     */
    default boolean tryLock(String key){
        return tryLock(key, getDefaultExpire(), getSleep(), getDefaultCount());
    }

    /**
     * 释放锁
     * @param key
     */
    void unlock(String key);
}
