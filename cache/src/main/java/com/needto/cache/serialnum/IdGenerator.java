package com.needto.cache.serialnum;

import com.needto.cache.entity.CacheData;
import com.needto.cache.redis.RedisCache;
import com.needto.common.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * redis 单机流水号生成器
 *
 * FIXME 多机情况下有问题
 *
 */
@Service
public class IdGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);

    @Autowired
    private RedisCache redisCache;

    /**
     * 下一个流水号
     * @param key 流水号前缀
     * @param ttl 过期时间
     * @param bit 流水号位数
     * @param ph 占位符
     * @return
     */
    public String build(String key, long ttl, Integer bit, String ph, CacheData cacheData) {
        Assert.validateStringEmpty(key, "key can not be empty");
        Assert.validateCondition(bit <= 0, "bit can not be less than 0");
        if(StringUtils.isEmpty(ph)){
            ph = "0";
        }
        long index;
        if(cacheData == null){
            index = redisCache.inc(key, ttl);
        }else{
            index = redisCache.inc(key, ttl, cacheData);
        }
        String temp = index + "";
        Assert.validateCondition(temp.length() > bit, "IDGENERATOR_MORE_THAN_MAX_BIT", "worksheetId has already exceeded max");

        StringBuilder prefix = new StringBuilder();
        for(int i = 0, len = bit - temp.length(); i < len; i++){
            prefix.append(ph);
        }
        LOG.debug("生成流水号：{}", prefix+temp);
        return prefix + temp;
    }

    /**
     * 默认生成5位的流水数
     * @param key
     * @param ttl
     * @return
     */
    public String build(String key, long ttl){
        return this.build(key, ttl, 5, "0", null);
    }
}
