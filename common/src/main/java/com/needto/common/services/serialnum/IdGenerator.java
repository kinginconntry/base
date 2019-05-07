package com.needto.common.services.serialnum;

import com.needto.common.cache.redis.RedisCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 * redis 流水号生成器
 *
 */
@Service
public class IdGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(IdGenerator.class);

    @Autowired
    private Environment environment;

    @Autowired
    private RedisCache redisCache;

    /**
     * 默认尾部流水数的占位符
     */
    private String defaultPh;

    /**
     * 尾部流水数长度
     */
    private Integer bit = 5;

    @PostConstruct
    public void init(){
        this.bit = Integer.valueOf(environment.getProperty("idgenerator.bit", "5"));
        this.defaultPh = environment.getProperty("idgenerator.ph", "0");
    }

    /**
     * @return 下一个流水号
     */
    public synchronized String build(String key, long ttl, int bit) {
        if(StringUtils.isEmpty(key)){
            throw new IllegalArgumentException("key can not be empty");
        }
        if(bit <= 0){
            throw new IllegalArgumentException("bit can not be less than 0");
        }
        long index = redisCache.inc(key, ttl);
        String temp = index + "";
        if(temp.length() > bit){
            throw new RuntimeException("worksheetId has already exceeded max");
        }
        StringBuilder prefix = new StringBuilder();
        for(int i = 0, len = bit - temp.length(); i < len; i++){
            prefix.append(defaultPh);
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
        return this.build(key, ttl, bit);
    }
}
