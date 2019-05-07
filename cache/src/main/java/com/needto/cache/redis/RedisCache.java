package com.needto.cache.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.needto.common.context.GlobalEnv;
import com.needto.common.exception.ValidateException;
import com.needto.common.utils.Assert;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 */
@Component
public class RedisCache{

    /**
     * 本地缓存
     */
    private final Cache<String, Object> M_LOCAL_CACHE = CacheBuilder.newBuilder()
            .maximumSize(2000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build();

    public static final String KEY_NAMESPACE_DELIMTER = ":";
    public static final String KEY_DELIMTER = ".";

    @Resource
    public RedisTemplate<String, Object> redisTemplate;

    private String prefix;

    private long defaultTtl;

    @PostConstruct
    public void init(){
        /**
         * 默认时间600秒
         */
        defaultTtl = Long.valueOf(GlobalEnv.getProperty("cache.ttl", "600"));
        /**
         * 默认前缀
         */
        prefix = GlobalEnv.getProperty("cache.prefix", "D");
    }

    /**
     * 获取某个应用下的key
     * @param keys
     * @return
     */
    public String buildKey(String... keys) {
        Objects.requireNonNull(keys);
        if (keys.length == 0) {
            throw new RuntimeException("缓存必须指定keys");
        }

        StringBuilder sb = new StringBuilder(this.prefix);
        sb.append(KEY_NAMESPACE_DELIMTER);
        boolean prevIsDelim = true;
        for (int i = 0; i < keys.length; i++) {
            String s = keys[i];
            boolean isDelim = KEY_DELIMTER.equals(s) || KEY_NAMESPACE_DELIMTER.equals(s);
            //不能有连续两个分隔符
            if (isDelim && prevIsDelim) {
                continue;
            }
            //连续两个不是分隔符的，添加一个分隔符
            if (!isDelim && !prevIsDelim) {
                sb.append(KEY_DELIMTER);
            }
            sb.append(s);
            prevIsDelim = isDelim;
        }
        return sb.toString();
    }

    // 公共
    public boolean hasKey(String key){
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.hasKey(key);
    }

    /**
     * 获取缓存过期时间
     * @param key
     * @param timeUnit
     * @return
     */
    public Long getExpire(String key, TimeUnit timeUnit){
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 设置过期时间（若为null，则不设置过期时间）
     * @param key 键
     * @param expire 过期时间（毫秒）
     */
    public void setExpire(String key, Long expire) {
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.MICROSECONDS);
        }
    }

    /**
     * 设置过期时间（若为null，则不设置过期时间）
     * @param key 键
     * @param expire 过期时间（秒）
     */
    public void setExpireSecond(String key, Long expire){
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置过期时间（若为null，则不设置过期时间）
     * @param key 键
     * @param expire 过期时间（分钟）
     */
    public void setExpireMinute(String key, Long expire){
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.MINUTES);
        }
    }

    /**
     * 设置过期时间（若为null，则不设置过期时间）
     * @param key 键
     * @param expire 过期时间（小时）
     */
    public void setExpireHour(String key, Long expire){
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.HOURS);
        }
    }

    /**
     * 设置过期时间（若为null，则不设置过期时间）
     * @param key 键
     * @param expire 过期时间（天）
     */
    public void setExpireDay(String key, Long expire){
        if (expire != null) {
            redisTemplate.expire(key, expire, TimeUnit.DAYS);
        }
    }

    /**
     * 删除
     * @param key 键
     * @return
     */
    public Boolean remove(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        if(!this.hasKey(key)){
            return true;
        }
        return redisTemplate.delete(key);
    }

    /**
     * 删除多个
     * @param keys 键
     * @return
     */
    public Long remove(Collection<String> keys) {
        Assert.validateCollectionEmpty(keys, "keys can not be empty");
       return redisTemplate.delete(keys);
    }

    // hash操作

    /**
     * 获取hash下的多个key
     * @param key
     * @return
     */
    public Set<String> hashKeys(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.<String, String>opsForHash().keys(key);
    }

    /**
     * 设置hash
     * @param prefix hash名
     * @param key hash键
     * @param object 对象
     * @param expire 过期时间
     * @return
     */
    public boolean setHash(String prefix, String key, Object object, Long expire) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        redisTemplate.opsForHash().put(prefix, key, object);
        this.setExpire(prefix, expire);
        return true;
    }

    public boolean setHash(String prefix, String key, Object object) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        return setHash(prefix, key, object, null);
    }

    /**
     * 设置多个hash
     * @param prefix hash名
     * @param data 数据map
     * @param expire 过期时间
     * @return
     */
    public boolean setHash(String prefix, Map<String, Object> data, Long expire) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateNull(data, "object can not be null");
        redisTemplate.opsForHash().putAll(prefix, data);
        this.setExpire(prefix, expire);
        return true;
    }

    public boolean setHash(String prefix, Map<String, Object> data) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateNull(data, "object can not be null");
        return setHash(prefix, data, null);
    }

    /**
     * 获取hash
     * @param prefix hash名
     * @param key hash键
     * @param <T>
     * @return
     */
    public <T> T getHash(String prefix, String key) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        return (T) redisTemplate.opsForHash().get(prefix, key);
    }

    public <T> T getHash(String prefix, String key, CacheData<T> cacheData) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        return this.getHash(prefix, key, defaultTtl, cacheData);
    }

    public boolean hasHashValue(String prefix, String key){
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        if(this.getHash(prefix, key) == null){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 注意ttl是对整个hash，而不是单个hashkey
     * @param prefix
     * @param key
     * @param ttl
     * @param cacheData
     * @param <T>
     * @return
     */
    public <T> T getHash(String prefix, String key, long ttl, CacheData<T> cacheData) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        Type type = ((ParameterizedType) cacheData.getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String val = (String) hashOperations.get(prefix, key);
        if (val == null) {
            T data = cacheData.get();
            this.setHash(prefix, key, data, ttl);
            return data;
        }
        return JSON.parseObject(val, type);
    }

    /**
     * 增加值,注意：如果原先不存在，则从initValueLoader中取值放入缓存并返回该值+delta
     * @param prefix
     * @param delta
     * @return
     */
    public long hashInc(String prefix, String key, long delta, long ttl, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        if (hashOperations.hasKey(prefix, key)) {
            return hashOperations.increment(prefix, key, delta);
        } else {
            Long value = initValueLoader.get() + delta;
            hashOperations.put(prefix, key, value);
            setExpire(prefix, ttl);
            return value;
        }
    }

    public long hashInc(String prefix, String key, long delta) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        return this.hashInc(prefix, key, delta, defaultTtl);
    }

    /**
     * 增加值,注意：必须有先调用hashPut,不然默认从0开始增长哦
     * @param prefix
     * @param delta
     * @return
     */
    public long hashInc(String prefix, String key, long delta, long ttl) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        return this.hashInc(prefix, key, delta, ttl, new CacheData<Long>() {
            @Override
            public Long get() {
                return 0L;
            }
        });
    }

    public long hashInc(String prefix, String key, long delta, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateStringEmpty(key, "key can not be empty");
        return this.hashInc(prefix, key, delta, defaultTtl, initValueLoader);
    }

    /**
     * 删除hash
     * @param prefix hash名
     * @param keys hash键集合
     * @return
     */
    public long removeHash(String prefix, List<String> keys) {
        Assert.validateStringEmpty(prefix, "prefix can not be empty");
        Assert.validateNull(keys, "keys can not be null");
        if(StringUtils.isEmpty(prefix) || CollectionUtils.isEmpty(keys)){
            return 0;
        }
        return redisTemplate.opsForHash().delete(prefix, keys.toArray());
    }

    // 对象操作

    /**
     * 设置对象
     * @param key
     * @param object 对象
     * @param expire 过期时间
     * @return
     */
    public boolean set(String key, Object object, Long expire) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        redisTemplate.opsForValue().set(key, object);
        this.setExpire(key, expire);
        return true;
    }

    public boolean set(String key, Object object) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        return set(key, object, null);
    }

    /**
     * 获取对象
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return (T) redisTemplate.opsForValue().get(key);
    }

    /**
     * 从缓存中获取值，缓存不存在时通过CacheData获取值
     *
     * @param key       要获取值的key
     * @param ttl       缓存key的过期时间
     * @param cacheData 缓存不存在时获取值的方法
     * @param force     强制刷新，即使缓存存在也重新获取值。
     * @param <T>       缓存值类型
     * @return 缓存中的值。
     */
    @SuppressWarnings("unchecked")
    private <T> T get(final String key, final long ttl, final CacheData<T> cacheData, final boolean force) {
        Assert.validateStringEmpty(key, "key can not be empty");
        T result;
        try {
            if (force) {
                remove(key);
            }
            //增加到本地内存缓存
            if (cacheData.cacheToLocal()) {
                result = (T) M_LOCAL_CACHE.get(key, (Callable<T>) () -> {
                    if (!hasKey(key)) {
                        T data = cacheData.get();
                        set(key, data, ttl);
                        return data;
                    }
                    return this.get(key);
                });
                //只缓存到redis
            } else {
                if (!hasKey(key) || force) {
                    T data = cacheData.get();
                    if(data != null) {
                        set(key, data, ttl);
                    }
                    result = data;
                } else {
                    result = this.get(key);
                }
            }
        } catch (ExecutionException e) {
            throw new UncheckedExecutionException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(final String key, final long ttl, final CacheData<T> cacheData) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return get(key, ttl, cacheData, false);
    }

    public <T> T get(String key, final CacheData<T> cacheData) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return get(key, defaultTtl, cacheData);
    }

    /**
     * 增加值,注意：如果实现不存在，则从initValueLoader中取值放入缓存并返回该值+delta
     * @param key
     * @param delta
     * @return
     */
    public long inc(String key, long delta, long ttl, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(key, "key can not be empty");
        long incred = redisTemplate.opsForValue().increment(key, delta);
        if(delta == incred) {
            //此时说明缓存的值增长前为0，要么就是0， 要么没有。对于大部分情况都是没有，则取值设置之
            Long value = initValueLoader.get() + delta;
            redisTemplate.opsForValue().set(key, value);
            setExpire(key, ttl);
            incred = value;
        }
        return incred;
    }

    public long inc(String key, long delta, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return this.inc(key, delta, defaultTtl, initValueLoader);
    }

    public long inc(String key, long delta) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long inc(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.opsForValue().increment(key, 1);
    }

    // list

    /**
     * 设置list
     * @param key
     * @param object 对象
     * @param expire 过期时间
     * @return
     */
    public boolean setList(String key, Object object, Long expire) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        redisTemplate.opsForList().rightPush(key, object);
        this.setExpire(key, expire);
        return true;
    }

    public boolean setList(String key, Object object) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        return setList(key, object, null);
    }

    /**
     * 设置list多个值
     * @param key
     * @param objects 对象集合
     * @param expire 过期时间
     * @return
     */
    public Long setList(String key, Collection<Object> objects, Long expire) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(objects, "collection can not be null");
        Long n = redisTemplate.opsForList().rightPushAll(key, objects);
        this.setExpire(key, expire);
        return n;
    }

    public Long setList(String key, Collection<Object> objects) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(objects, "collection can not be null");
        return setList(key, objects, null);
    }

    /**
     * 替换集合中的某个元素
     * @param key 键
     * @param index 元素位置
     * @param newObj 新对象
     * @return
     */
    public boolean replaceList(String key, long index, Object newObj){
        Assert.validateStringEmpty(key, "key can not be empty");
        if(!redisTemplate.hasKey(key)){
            return false;
        }
        redisTemplate.opsForList().set(key, index, newObj);
        return true;
    }

    /**
     * 获取list
     * @param key
     * @param index 位置(从0开始)
     * @param <T>
     * @return
     */
    public <T> T getList(String key, long index) {
        Assert.validateStringEmpty(key, "key can not be empty");
        List<T> list = (List<T>) redisTemplate.opsForList().range(key, index, index);
        if(list.size() > 0){
            return list.get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取list多个值
     * @param key
     * @param start 开始位置（从0开始）
     * @param end 结束位置
     * @param <T>
     * @return
     */
    public <T> List<T> getList(String key, long start, long end){
        Assert.validateStringEmpty(key, "key can not be empty");
        return (List<T>) redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 获取list长度
     * @param key
     * @return
     */
    public Long listSize(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 删除1个或多个list的值
     * @param key
     * @param count 要删除前几个
     * @param object 删除的对象是什么
     * @return
     */
    public Long removeList(String key, long count, Object object){
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateNull(object, "object can not be null");
        if(count < 0){
            throw new ValidateException("count can not less than 0");
        }
       return redisTemplate.opsForList().remove(key, count, object);
    }

    /**
     * 从前面弹出元素
     * @param key
     * @param <T>
     * @return
     */
    public <T> T leftPop(String key){
        Assert.validateStringEmpty(key, "key can not be empty");
        return (T) redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从尾部弹出元素
     * @param key
     * @param <T>
     * @return
     */
    public <T> T rightPop(String key){
        Assert.validateStringEmpty(key, "key can not be empty");
        return (T) redisTemplate.opsForList().rightPop(key);
    }

    public long size(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        DataType dataType = redisTemplate.type(key);
        if (dataType == DataType.HASH) {
            return redisTemplate.opsForHash().size(key);
        } else if (dataType == DataType.LIST) {
            return redisTemplate.opsForList().size(key);
        } else {
            return redisTemplate.opsForValue().size(key);
        }
    }

    public Set<String> localKeys() {
        return M_LOCAL_CACHE.asMap().keySet();
    }

    /**
     * 仅删除本地缓存
     *
     * @param key key
     */
    public void deleteLocal(String key) {
        Assert.validateStringEmpty(key, "key can not be empty");
        M_LOCAL_CACHE.invalidate(key);
    }

    public Set<String> keys(String pattern) {
        Assert.validateNull(pattern, "pattern can not be null");
        return redisTemplate.keys(pattern);
    }

}
