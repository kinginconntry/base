package com.needto.cache.asyncapi;

import com.needto.cache.redis.RedisCache;
import com.needto.common.exception.LogicException;
import com.needto.common.exception.ValidateException;
import com.needto.common.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 异步api结果处理容器
 */
@Service
public class AsyncApiResultService {

    /**
     * 异步结果前缀
     */
    public final static String ASYNC_RES = "ASYNC_RES";

    @Autowired
    private RedisCache redisCache;

    public String getKey(String guid){
        return redisCache.buildKey(ASYNC_RES + ":" + guid);
    }

    /**
     * 获取结果
     * @param guid
     * @param <T>
     * @return
     */
    public <T> AsyncResult<T> getResult(String guid){
        Assert.validateStringEmpty(guid, "guid can not be null");
        String key = this.getKey(guid);
        AsyncResult<T> res = redisCache.get(key);
        if(res == null){
            throw new LogicException("NO_ASYNC_TASK", "");
        }
        return res;
    }

    /**
     * 设置任务进度
     * @param guid
     * @param <T>
     * @return
     */
    public synchronized <T> AsyncResult<T> incPercent(String guid, int inc, Long expire){
        Assert.validateStringEmpty(guid, "guid can not be null");
        if(inc < 0){
            throw new ValidateException("inc can not less than 0");
        }
        AsyncResult<T> res = this.getResult(guid);
        if(res != null){
            if(!res.isSuccess()){
                res.incPercent(inc);
                this.setResult(guid, res.getValue(), expire, true);
            }
        }
        return res;
    }

    /**
     * 设置结果
     * @param guid
     * @param res
     * @param expire
     * @param <T>
     * @return
     */
    public <T> boolean setResult(String guid, T res, Long expire){
        return this.setResult(guid, res, expire, true);
    }

    /**
     * 初始化异步任务
     * @param guid
     * @param expire
     * @param replace
     * @return
     */
    public boolean initResult(String guid, Long estimatedTime, Long expire, boolean replace){
        return this.setResult(guid, null, expire, estimatedTime, replace);
    }

    public boolean initResult(String guid, Long expire, boolean replace){
        return this.setResult(guid, null, expire, null, replace);
    }

    /**
     * 设置异步任务
     * @param guid
     * @param res
     * @param expire
     * @param replace
     * @param <T>
     * @return
     */
    public <T> boolean setResult(String guid, T res, Long expire, Long estimatedTime, boolean replace){
        Assert.validateStringEmpty(guid, "guid can not be null");
        String key = this.getKey(guid);
        AsyncResult<T> cache;
        if(redisCache.hasKey(key)){
            if(!replace){
                throw new LogicException("EXIST_TASK", "guid had exist");
            }
            cache = this.getResult(guid);
        }else{
            cache = new AsyncResult<>();
        }
        cache.setValue(res);
        cache.setEstimatedTime(estimatedTime);
        return redisCache.set(key, cache, expire);
    }

    public <T> boolean setResult(String guid, T res, Long expire, boolean replace){
        return this.setResult(guid, res, expire, replace);
    }
}
