package com.needto.cache.frequency;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.needto.cache.entity.CacheData;
import com.needto.cache.redis.RedisCache;
import com.needto.tool.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 *
 * 访问频率控制器
 */
@Service
public class FrequencyService {

    private static final Logger LOG = LoggerFactory.getLogger(FrequencyService.class);

    /**
     * 频率拦截key前缀，前缀为该key的缓存，则直接禁用
     */
    private static final String CACHE_PREFIX_KEY = "_FREQ_FILTER";

    /**
     * 频率计数器key，计算源访问目标的次数，达到一定次数就进行禁用，直到禁用过期
     */
    private static final String CACHE_PREFIX_KEY_INC = "_FREQ_INC";

    /**
     * 同步锁
     */
    private static final Cache<String, Lock> LOCK_CACHE = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    @Autowired
    private RedisCache redisCache;

    /**
     * 检查单位时间内源对目标的访问次数，达到max就进行禁用，禁用一段时间后自动解封
     * @param source 禁用源
     * @param target 被禁用目标
     * @param max 最大次数
     * @param second 单位时间 秒
     * @param forbidTime 禁用时间 秒
     * @return false 不禁用， true 禁用
     */
    public boolean filter(String source, String target, int max, long second, long forbidTime){
        Assert.validateStringEmpty(source);
        Assert.validateStringEmpty(target);
        Lock lock = null;
        try {
            lock = LOCK_CACHE.get(target, ReentrantLock::new);
            if(lock.tryLock(200, TimeUnit.MILLISECONDS)){
                if(redisCache.get(redisCache.buildKey(CACHE_PREFIX_KEY , source, target)) != null){
                    LOG.debug("频率禁用期间，source：{}，target：{}，max {}， second {}，forbidTime {}", source, target, max, second, forbidTime);
                    return true;
                }
                Long i = redisCache.inc(redisCache.buildKey(CACHE_PREFIX_KEY_INC , source, target), 1, second, new CacheData<Long>() {
                    @Override
                    public Long get() {
                        return 1L;
                    }
                });
                if(i <= max){
                    return false;
                }else{
                    LOG.debug("开始进入禁用期间，source：{}，target：{}，max {}， second {}，forbidTime {}", source, target, max, second, forbidTime);
                    redisCache.set(redisCache.buildKey(CACHE_PREFIX_KEY , source, target), 1, forbidTime);
                    redisCache.remove(redisCache.buildKey(CACHE_PREFIX_KEY_INC , source, target));
                    return true;
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(lock != null){
                lock.unlock();
            }
        }
        return true;
    }

}
