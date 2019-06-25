package com.needto.common.entity;


/**
 * 可缓存的数据加载器
 * @author Administrator
 */
public abstract class CacheData<T>{
    /**
     * 从持久化加载数据，加载后的数据将被缓存
     *
     * @return
     */
    public abstract T get();

    /**
     * 是否缓存到本地内存缓存。默认为false，对于高频读、低频写的数据，应当缓存到本地。
     * @return
     */
    public boolean cacheToLocal() {
        return false;
    }
}
