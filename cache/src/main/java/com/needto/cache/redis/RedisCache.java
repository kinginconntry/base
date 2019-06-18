package com.needto.cache.redis;

import com.google.common.collect.Lists;
import com.needto.cache.entity.CacheData;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * 设置，更新，删除数据时，发送redis消息，通知其他机器也删除本地缓存
 */
@Component
public class RedisCache<V> {

    private static final String KEY_NAMESPACE_DELIMTER = ":";

    private static final String KEY_DELIMTER = ".";

    @Resource
    private Environment environment;

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    private String prefix;

    private long defaultTtl;

    private String topic;

    @PostConstruct
    public void init(){
        // 默认时间600秒
        defaultTtl = Long.valueOf(environment.getProperty("rediscache.ttl", "600"));
        // 默认前缀
        prefix = environment.getProperty("rediscache.prefix", "D");
        // 默认的消息topic
        topic = environment.getProperty("rediscache.topic", "TOPIC:REDIS_CACHE");
    }

    public String getTopic(){
        return topic;
    }

    /**
     * 删除key消息（发布者）
     * @param keys 键
     */
    public void optDeleteKeys(Collection<String> keys){
        if(!CollectionUtils.isEmpty(keys)){
            redisTemplate.convertAndSend(topic, new RedisInvalidMessage(Lists.newArrayList(keys)));
        }
    }

    /**
     * 获取某个应用下的key
     * @param  keys 键
     * @return 数据
     */
    public String buildKey(String... keys) {
        Objects.requireNonNull(keys);
        if (keys.length == 0) {
            throw new LogicException("NO_KEYS", "keys can not be empty");
        }

        StringBuilder sb = new StringBuilder(this.prefix);
        sb.append(KEY_NAMESPACE_DELIMTER);
        boolean prevIsDelim = true;
        for (String s : keys) {
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

    /**
     *
     * @param key 键
     * @return 数据
     */
    public boolean hasKey(String key){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Boolean flag =  redisTemplate.hasKey(key);
        return flag != null && flag;
    }

    /**
     * 获取缓存过期时间
     * @param key 键
     * @param timeUnit 时间类型
     * @return 数据
     */
    public Long getExpire(String key, TimeUnit timeUnit){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
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
     * @return 数据
     */
    public Boolean remove(String key) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        if(!this.hasKey(key)){
            return true;
        }
        Boolean flag = redisTemplate.delete(key);
        if(flag != null && flag){
            optDeleteKeys(Lists.newArrayList(key));
        }
        return flag;
    }

    /**
     * 删除多个
     * @param keys 键
     * @return 数据
     */
    public Long remove(Collection<String> keys) {
        Assert.validateCollectionEmpty(keys, "NO_KEYS", "keys can not be empty");
        Long flag = redisTemplate.delete(keys);
        if(flag != null && flag > 0){
            optDeleteKeys(Lists.newArrayList(keys));
        }
        return flag;
    }

    // hash操作

    /**
     * 获取hash下的多个key
     * @param key 键
     * @return 数据
     */
    public Set<String> hashKeys(String key) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        HashOperations<String, String, V> operations = redisTemplate.opsForHash();
        return operations.keys(key);
    }

    /**
     * 设置hash
     * @param prefix hash名
     * @param key hash键
     * @param object 对象
     * @param expire 过期时间
     * @return 数据
     */
    public boolean setHash(String prefix, String key, Object object, Long expire) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        redisTemplate.opsForHash().put(prefix, key, object);
        this.setExpire(prefix, expire);
        this.optDeleteKeys(Lists.newArrayList(prefix));
        return true;
    }

    public boolean setHash(String prefix, String key, Object object) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        return setHash(prefix, key, object, null);
    }

    /**
     * 设置多个hash
     * @param prefix hash名
     * @param data 数据map
     * @param expire 过期时间
     * @return 数据
     */
    public boolean setHash(String prefix, Map<String, V> data, Long expire) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateNull(data, "NO_DATA", "object can not be null");
        redisTemplate.opsForHash().putAll(prefix, data);
        this.setExpire(prefix, expire);
        this.optDeleteKeys(Lists.newArrayList(prefix));
        return true;
    }

    public boolean setHash(String prefix, Map<String, V> data) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateNull(data, "NO_DATA", "object can not be null");
        return setHash(prefix, data, null);
    }

    /**
     * 获取hash
     * @param prefix hash名
     * @param key hash键
     * @return 数据
     */
    public V getHash(String prefix, String key) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        HashOperations<String, String, V> operations = redisTemplate.opsForHash();
        return operations.get(prefix, key);
    }

    public V getHash(String prefix, String key, CacheData<V> cacheData) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.getHash(prefix, key, defaultTtl, cacheData);
    }

    public boolean existsHashValue(String prefix, String key){
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.getHash(prefix, key) != null;
    }

    /**
     * 注意ttl是对整个hash，而不是单个hashkey
     * @param prefix 键
     * @param key hash key
     * @param ttl 过期时间
     * @param cacheData 回调
     * @return 数据
     */
    public V getHash(String prefix, String key, long ttl, CacheData<V> cacheData) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        HashOperations<String, String, V> hashOperations = redisTemplate.opsForHash();
        V val =  hashOperations.get(prefix, key);
        if (val == null) {
            V data = cacheData.get();
            this.setHash(prefix, key, data, ttl);
            return data;
        }else{
            return val;
        }
    }

    /**
     * 增加值,注意：如果原先不存在，则从initValueLoader中取值放入缓存并返回该值+delta
     * @param prefix 键
     * @param delta 步长
     * @return 数据
     */
    public long hashInc(String prefix, String key, long delta, long ttl, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        HashOperations<String, String, Long> hashOperations = redisTemplate.opsForHash();
        if (hashOperations.hasKey(prefix, key)) {
            Long flag = hashOperations.increment(prefix, key, delta);
            setExpire(prefix, ttl);
            this.optDeleteKeys(Lists.newArrayList(prefix));
            return flag;
        } else {
            Long value = initValueLoader.get();
            hashOperations.put(prefix, key, value);
            setExpire(prefix, ttl);
            this.optDeleteKeys(Lists.newArrayList(prefix));
            return value;
        }
    }

    public long hashInc(String prefix, String key, long delta) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.hashInc(prefix, key, delta, defaultTtl);
    }

    /**
     * 增加值,注意：必须有先调用hashPut,不然默认从0开始增长哦
     * @param prefix 键
     * @param delta 步长
     * @return 数据
     */
    public long hashInc(String prefix, String key, long delta, long ttl) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.hashInc(prefix, key, delta, ttl, new CacheData<Long>() {
            @Override
            public Long get() {
                return 0L;
            }
        });
    }

    public long hashInc(String prefix, String key, long delta, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.hashInc(prefix, key, delta, defaultTtl, initValueLoader);
    }

    /**
     * 删除hash
     * @param prefix hash名
     * @param keys hash键集合
     * @return 数据
     */
    public long removeHash(String prefix, List<String> keys) {
        Assert.validateStringEmpty(prefix, "NO_PREFIX", "prefix can not be empty");
        Assert.validateNull(keys, "NO_KEYS", "keys can not be null");
        if(StringUtils.isEmpty(prefix) || CollectionUtils.isEmpty(keys)){
            return 0;
        }
        Long flag = redisTemplate.opsForHash().delete(prefix, keys.toArray());
        if(flag > 0){
            this.optDeleteKeys(keys);
        }
        return flag;
    }

    // 对象操作

    /**
     * 设置对象
     * @param key 键
     * @param object 对象
     * @param expire 过期时间
     * @return 数据
     */
    public boolean set(String key, V object, Long expire) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        redisTemplate.opsForValue().set(key, object);
        this.setExpire(key, expire);
        this.optDeleteKeys(Lists.newArrayList(key));
        return true;
    }

    public boolean set(String key, V object) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        return set(key, object, null);
    }

    /**
     * 获取对象
     * @param key 键
     * @return 数据
     */
    public V get(String key) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return redisTemplate.opsForValue().get(key);
    }

    public V get(String key, long ttl, CacheData<V> callable) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        V obj = redisTemplate.opsForValue().get(key);
        if(obj == null){
            obj = callable.get();
            this.set(key, obj);
            this.setExpire(key, ttl);
            this.optDeleteKeys(Lists.newArrayList(key));
        }
        return obj;
    }

    /**
     * 增加值,注意：如果实现不存在，则从initValueLoader中取值放入缓存并返回该值+delta
     * @param key 键
     * @param delta 步长
     * @return 数据
     */
    public long inc(String key, long delta, long ttl, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");

        if(this.hasKey(key)) {
            Long value = redisTemplate.opsForValue().increment(key, delta);
            redisTemplate.opsForValue().set(key, (V) value);
            setExpire(key, ttl);
            this.optDeleteKeys(Lists.newArrayList(key));
            return value;
        }else{
            Long value = initValueLoader.get();
            redisTemplate.opsForValue().set(key, (V) value);
            setExpire(key, ttl);
            this.optDeleteKeys(Lists.newArrayList(key));
            return value;
        }
    }

    public long inc(String key, long delta, CacheData<Long> initValueLoader) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.inc(key, delta, defaultTtl, initValueLoader);
    }

    public Long inc(String key, long delta, long ttl) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Long value = redisTemplate.opsForValue().increment(key, delta);
        this.setExpire(key, ttl);
        this.optDeleteKeys(Lists.newArrayList(key));
        return value;
    }

    public Long inc(String key, long ttl) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return this.inc(key, 1, ttl);
    }

    // list

    /**
     * 设置list
     * @param key 键
     * @param object 对象
     * @param expire 过期时间
     * @return 数据
     */
    public boolean setList(String key, V object, Long expire) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        Long flag = redisTemplate.opsForList().rightPush(key, object);
        if(flag != null && flag > 0){
            this.setExpire(key, expire);
            this.optDeleteKeys(Lists.newArrayList(key));
            return true;
        }else{
            return false;
        }
    }

    public boolean setList(String key, V object) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        return setList(key, object, null);
    }

    /**
     * 设置list多个值
     * @param key 键
     * @param objects 对象集合
     * @param expire 过期时间
     * @return 数据
     */
    public Long setList(String key, Collection<V> objects, Long expire) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(objects, "NO_DATA", "collection can not be null");
        Long n = redisTemplate.opsForList().rightPushAll(key, objects);
        if(n != null && n > 0){
            this.setExpire(key, expire);
            this.optDeleteKeys(Lists.newArrayList(key));
        }
        return n;
    }

    public Long setList(String key, Collection<V> objects) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(objects, "NO_DATA", "collection can not be null");
        return setList(key, objects, null);
    }

    /**
     * 替换集合中的某个元素
     * @param key 键
     * @param index 元素位置
     * @param newObj 新对象
     * @return 数据
     */
    public boolean replaceList(String key, long index, V newObj){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        if(!this.hasKey(key)){
            return false;
        }
        redisTemplate.opsForList().set(key, index, newObj);
        this.optDeleteKeys(Lists.newArrayList(key));
        return true;
    }

    /**
     * 获取list
     * @param key 键
     * @param index 位置(从0开始)
     * @return 数据
     */
    public V getList(String key, long index) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        List<V> list = redisTemplate.opsForList().range(key, index, index);
        if(!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }else{
            return null;
        }
    }

    /**
     * 获取list多个值
     * @param key 键
     * @param start 开始位置（从0开始）
     * @param end 结束位置
     * @return 数据
     */
    public List<V> getList(String key, long start, long end){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return redisTemplate.opsForList().range(key, start, end);
    }


    /**
     * 获取list长度
     * @param key 键
     * @return 数据
     */
    public Long listSize(String key) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 删除1个或多个list的值
     * @param key 键
     * @param count 要删除前几个
     * @param object 删除的对象是什么
     * @return 数据
     */
    public Long removeList(String key, long count, Object object){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateNull(object, "NO_DATA", "object can not be null");
        Assert.validateCondition(count < 0, "count can not less than 0");
        Long value = redisTemplate.opsForList().remove(key, count, object);
        if(value != null && value > 0){
            this.optDeleteKeys(Lists.newArrayList(key));
        }
        return value;
    }

    /**
     * 从前面弹出元素
     * @param key 键
     * @return 数据
     */
    public V leftPop(String key){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        V obj = redisTemplate.opsForList().leftPop(key);
        if(obj != null){
            this.optDeleteKeys(Lists.newArrayList(key));
        }
        return obj;
    }

    /**
     * 从尾部弹出元素
     * @param key 键
     * @return 数据
     */
    public V rightPop(String key){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        V obj = redisTemplate.opsForList().rightPop(key);
        if(obj != null){
            this.optDeleteKeys(Lists.newArrayList(key));
        }
        return obj;
    }

    // set

    // zset

    /**
     * 添加zset数据
     * @param key
     * @param data
     * @param score
     * @param expire
     * @return
     */
    public boolean setZset(String key, V data, double score, Long expire){
        redisTemplate.opsForZSet().add(key, data, score);
        this.setExpire(key, expire);
        this.optDeleteKeys(Lists.newArrayList(key));
        return true;
    }

    /**
     * 获取数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<V> getZset(String key, long start, long end){
        return Lists.newArrayList(Objects.requireNonNull(redisTemplate.opsForZSet().range(key, start, end)));
    }

    /**
     * 按照分数排名
     * @param key key
     * @param start 开始位置
     * @param end 结束位置
     * @param desc 逆序排
     * @return
     */
    public List<V> getRank(String key, long start, long end, boolean desc){
        Set<ZSetOperations.TypedTuple<V>> set;
        if(desc){
            set = redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
        }else{
            set = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
        }
        List<V> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(set)){
            set.forEach(v -> list.add(v.getValue()));
        }
        return list;
    }

    /**
     * 删除某些数据
     * @param key
     * @param datas
     * @return
     */
    public Long deleteZset(String key, List<V> datas){
        Long c = redisTemplate.opsForZSet().remove(key, datas);
        this.optDeleteKeys(Lists.newArrayList(key));
        return c;
    }

    /**
     * 删除某个索引段的数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Long deleteZset(String key, long start, long end){
        Long c = redisTemplate.opsForZSet().removeRange(key, start, end);
        this.optDeleteKeys(Lists.newArrayList(key));
        return c;
    }

    /**
     * 删除某个分数段的数据
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long deleteZset(String key, double min, double max){
        Long c = redisTemplate.opsForZSet().removeRangeByScore(key, min, max);
        this.optDeleteKeys(Lists.newArrayList(key));
        return c;
    }

    /**
     * 获取某个key的对应值得个数
     * @param key 键
     * @return
     */
    public Long size(String key) {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        DataType dataType = redisTemplate.type(key);
        if (dataType == DataType.HASH) {
            return redisTemplate.opsForHash().size(key);
        } else if (dataType == DataType.LIST) {
            return redisTemplate.opsForList().size(key);
        } else {
            return redisTemplate.opsForValue().size(key);
        }
    }

    /**
     * 正则查找符合条件的key
     * @param pattern 正则
     * @return
     */
    public Set<String> keys(String pattern) {
        Assert.validateNull(pattern, "NO_PATTERN", "pattern can not be null");
        return redisTemplate.keys(pattern);
    }

    /**
     * 执行lua脚本
     * @param scriptStr 脚本字符串
     * @param objClass 返回值类型
     * @param keys 键key
     * @param args 脚本可使用的参数
     * @param <T> 返回值泛型
     * @return 返回值
     */
    public <T> T executeScript(String scriptStr, Class<T> objClass, List<String> keys, Object... args){
        DefaultRedisScript<T> script = new DefaultRedisScript<>();
        script.setResultType(objClass);
        script.setScriptText(scriptStr);
        return redisTemplate.execute(script, keys, args);
    }
}
