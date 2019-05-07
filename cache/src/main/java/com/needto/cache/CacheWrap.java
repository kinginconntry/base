package com.needto.cache;

import java.io.Serializable;

/**
 * @author Administrator
 * 缓存包裹，可以判断是否为空缓存
 */
public class CacheWrap<T> implements Serializable {

    public T data;

    public static <T> CacheWrap<T> wrap(T data){
        CacheWrap<T> cacheWrap = new CacheWrap<>();
        cacheWrap.setData(data);
        return cacheWrap;
    }

    public boolean isNull() {
        return this.data == null;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
