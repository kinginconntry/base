package com.needto.notice.service;

import com.needto.cache.entity.CacheData;
import com.needto.cache.entity.CacheWrap;
import com.needto.cache.redis.RedisCache;
import com.needto.common.entity.Target;
import com.needto.notice.data.Constant;
import com.needto.notice.event.NoticeChangeEvent;
import com.needto.notice.model.Notice;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NoticeCache {

    private static final String NOTICE_KEY_PREFIX = "_notice";

    @Autowired
    private Environment environment;

    @Autowired
    private RedisCache<CacheWrap<List<Notice>>> redisCache;

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

    /**
     * 获取通用的
     * @return
     */
    public List<Notice> getCommon(){
        String key = redisCache.buildKey(NOTICE_KEY_PREFIX, Constant.ALL_USER.getTargetId());
        CacheWrap<List<Notice>> cacheWrap = redisCache.get(key, expire, new CacheData<CacheWrap<List<Notice>>>() {
            @Override
            public CacheWrap<List<Notice>> get() {
                List<Notice> notices = noticeService.getNotice(Constant.ALL_USER);
                return CacheWrap.wrap(notices);
            }
        });
        return cacheWrap.getData();
    }

    /**
     * 获取用户自己的相关通知
     * @param target
     * @return
     */
    public List<Notice> getUserNotices(Target target){
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

    /**
     * 获取所有通知
     * @param target
     * @return
     */
    public List<Notice> getNotices(Target target){
        List<Notice> common = getCommon();
        List<Notice> user = getUserNotices(target);
        List<Notice> all = new ArrayList<>();
        if(common != null){
            all.addAll(common);
        }
        if(user != null){
            all.addAll(user);
        }
        if(!CollectionUtils.isEmpty(all)){
            Iterator<Notice> iterator = all.iterator();
            long now = System.currentTimeMillis();
            while (iterator.hasNext()){
                Notice temp = iterator.next();
                if(temp.getStartTime() != null && temp.getStartTime().getTime() > now){
                    iterator.remove();
                }
                if(temp.getEndTime() != null && temp.getEndTime().getTime() < now){
                    iterator.remove();
                }
            }
        }
        return all;
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
