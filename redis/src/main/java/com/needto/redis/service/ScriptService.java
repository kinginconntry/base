package com.needto.redis.service;

import com.google.common.collect.Lists;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;

/**
 * redis脚本相关的通用脚本方法
 */
@Service
public class ScriptService {

    @Autowired
    private RedisCache redisCache;

    // 以下全为分布式下的简洁方法

    /**
     * 分布式下产生唯一编号
     * @param key 键
     * @return
     */
    public String buildNo(String key, long startNo){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return (String) redisCache.executeScript("local temp = redis.call('get', KEYS[1]) " +
                        "if temp then redis.call('set', KEYS[1], temp + 1) return tostring(temp + 1)  else redis.call('set', KEYS[1], ARGV[1]) return tostring(ARGV[1]) end",
                String.class,
                Lists.newArrayList(key),
                startNo);
    }

    // 分布式锁

    /**
     * 设置锁
     * @param key
     * @return 若返回时一个正数，标识上锁成功，后续解锁时使用该正数，若返回-1，则表示没有获取到锁
     */
    public Long tryLock(String key){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        return (Long) redisCache.executeScript("local temp = os.time() if redis.call('get', KEYS[1]) then return -1 else redis.call('set', KEYS[1], temp) return temp end",
                Long.class,
                Lists.newArrayList(key));
    }

    /**
     * 解锁
     * @param key
     * @param version
     */
    public void unlock(String key, long version){
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        redisCache.executeScript("if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end", Void.class, Lists.newArrayList(key), version);
    }

    /**
     * 分布式同步处理
     * @param key 键
     * @param retryCount 重试次数
     * @param waitTime 每次重试之前的等待时间 毫秒
     * @param callable 回调
     * @param <T> 返回值类型
     * @return 返回值
     */
    public <T> T sync(String key, int retryCount, long waitTime, Callable<T> callable) throws Exception {
        Assert.validateStringEmpty(key, "NO_KEY", "key can not be empty");
        Assert.validateCondition(retryCount < 1, "retryCount can not be less than 1");
        Assert.validateCondition(waitTime <= 0, "waitTime can not be less than 1");
        Long lockVersion = null;
        try {
            int n = 0;
            while (true){
                n++;
                if(n > retryCount){
                    throw new Exception("retry failture");
                }
                lockVersion = tryLock(key);
                if(lockVersion == null){
                    Thread.sleep(waitTime);
                }
                return callable.call();
            }
        }catch (Exception e){
            throw e;
        }finally {
            if(lockVersion != null){
                this.unlock(key, lockVersion);
            }
        }
    }

    public <T> T sync(String key, Callable<T> callable) throws Exception {
        return sync(key, 3, 1000, callable);
    }
}
