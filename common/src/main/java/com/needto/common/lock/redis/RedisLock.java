package com.needto.common.lock.redis;

import com.needto.common.exception.LogicException;
import com.needto.common.lock.ILock;
import com.needto.common.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 利用redis实现全局排他锁.
 *  * 一般常见的使用形式是：<pre>
 * <code>
 *  GlobalReclusiveLock lock = new GlobalReclusiveLock("xxx", 500);
 *  if (lock.tryLock()) {
 *      try{
 *         // do something here.
 *      } finally {
 *          lock.unLock();
 *      }
 *  }
 * </code>
 *</pre>
 *
 * <strong>注意：</strong> 应用程序应该设置合理的过期时间<strong>duration</strong>, 过期时间设置的太短可能导致业务尚未完成，
 * 锁已经过期而被其他应用获得，从而出现多个应用并行的异常情况.
 * Created by Administrator on 2017/12/4 0004.
 */
@Component
public class RedisLock implements ILock {

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public boolean tryLock(String key, long expire, long sleep, int count) {

        //设置成功，获得全局锁，可进行业务处理
        Assert.validateCondition(count <= 0);
        Assert.validateCondition(expire <= 0);
        Assert.validateCondition(sleep <= 0);

        /**
         * 尝试获取锁
         */
        int i = 0;
        while (i < count) {
            boolean success = redisTemplate.opsForValue().setIfAbsent(key, System.currentTimeMillis() + expire);
            if (success) {
                return true;
            }
            i++;
            try {
                TimeUnit.MILLISECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                throw new LogicException("", "");
            }
        }

        //检查锁是否已经过期, 这可能是其他客户端由于异常情况未成功释放全局锁导致.
        long value = Long.valueOf(redisTemplate.opsForValue().get(key) + "");
        long now = System.currentTimeMillis();
        if (value >= now) {
            this.unlock(key);
            this.tryLock(key, expire, sleep, 1);
        }
        return false;
    }

    @Override
    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
