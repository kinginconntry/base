package com.needto.notice.service;

import com.needto.cache.entity.CacheData;
import com.needto.cache.entity.CacheWrap;
import com.needto.cache.redis.RedisCache;
import com.needto.common.entity.Target;
import com.needto.common.utils.Assert;
import com.needto.notice.event.NoticeChangeEvent;
import com.needto.notice.model.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeCache {

    private static final String NOTICE_KEY_PREFIX = "_notice";

    @Autowired
    private Environment environment;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private NoticeService noticeService;

    /**
     * 过期时间
     */
    private long expire;

    @PostConstruct
    public void init(){
        // 默认一天
        this.expire = Long.valueOf(environment.getProperty("notice.expire", "86400000"));
    }

    @EventListener
    public void noticeChange(NoticeChangeEvent noticeChangeEvent){
        this.remove(noticeChangeEvent.targets);
    }

    public List<Notice> getNotices(Target target){
        Assert.validateNull(target);
        String key = redisCache.buildKey(NOTICE_KEY_PREFIX, target.getTargetId());
        CacheWrap<List<Notice>> cacheWrap = redisCache.get(key, expire, new CacheData<CacheWrap<List<Notice>>>() {
            @Override
            public CacheWrap<List<Notice>> get() {
                List<Notice> notices = noticeService.getNotice(target);
                return CacheWrap.wrap(notices);
            }
        });
        return cacheWrap.getData();
    }

    private void remove(List<Target> targets){
        if(CollectionUtils.isEmpty(targets)){
            return;
        }
        List<String> keys = new ArrayList<>();
        targets.forEach(v -> keys.add(redisCache.buildKey(NOTICE_KEY_PREFIX, v.getTargetId())));
        redisCache.remove(keys);
    }
}
