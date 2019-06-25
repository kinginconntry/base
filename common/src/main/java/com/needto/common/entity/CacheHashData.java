/**
 * 
 */
package com.needto.common.entity;


import java.util.Collection;
import java.util.Map;

/**
 * 可缓存的数据加载器,加载map类型的数据，key限制为字符串类型
 * @author Administrator
 */
public abstract class CacheHashData<T> extends CacheData<Map<String, T>> {
    /**
     * 执行方法返回的结果
     *
     * @return
     */
    public abstract Map<String, T> get(Collection<String> hashKeys);
}
