package com.needto.firewall.list;

import com.needto.cache.CacheWrap;
import com.needto.cache.redis.CacheData;
import com.needto.cache.redis.RedisCache;
import com.needto.common.entity.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Administrator
 * 名单缓存
 */
@Component
public class ListCache {

    private static final CacheWrap<Data> DEFAULT_CACHE = new CacheWrap<>();

    public static class Data {
        public Long start;
        public Long end;

        public Data(Long start, Long end) {
            this.start = start;
            this.end = end;
        }
    }

    /**
     * 默认的缓存过期时间: 1小时
     */
    public static final Long DEFAULT_CACHE_EXPIRE = (long) 60 * 60;

    /**
     * 默认的名单缓存前缀
     */
    public static final String DEFAULT_LIST_CACHE = "_LIST_";

    @Autowired
    private FilterListService filterListService;

    @Autowired
    private RedisCache redisCache;

    public Long getExpire() {
        // 小幅度改变缓存过期时间，防止缓存同时失效，幅度 0 - 600 秒
        return DEFAULT_CACHE_EXPIRE + new Random().nextInt(600);
    }

    public String buildCacheKey(String mode, String type, String guid) {

        return redisCache.buildKey(DEFAULT_LIST_CACHE + ":" + mode, type, guid);
    }

    public void set(SetListEvent setListEvent) {
        FilterList filterList = setListEvent.getFilterList();
        redisCache.set(this.buildCacheKey(filterList.getMode(), filterList.getTarget().getType(), filterList.getTarget().getGuid()), CacheWrap.wrap(new Data(filterList.getStart().getTime(), filterList.getEnd() != null ? filterList.getEnd().getTime() : null)), getExpire());
    }

    public Data get(String mode, Target target) {
        if (StringUtils.isEmpty(mode) || target == null || StringUtils.isEmpty(target.getType()) || StringUtils.isEmpty(target.getGuid())) {
            return null;
        }
        CacheWrap<Data> cacheWrap = redisCache.get(this.buildCacheKey(mode, target.getType(), target.getGuid()), getExpire(), new CacheData<CacheWrap<Data>>() {
            @Override
            public CacheWrap<Data> get() {
                FilterList filterList = filterListService.findValid(mode, target);
                if (filterList == null) {
                    return DEFAULT_CACHE;
                }
                return CacheWrap.wrap(new Data(filterList.getStart().getTime(), filterList.getEnd() != null ? filterList.getEnd().getTime() : null));
            }
        });
        return cacheWrap.getData();
    }

    @EventListener
    public void remove(RemoveListEvent removeListEvent) {
        List<FilterList> filterListList = removeListEvent.getFilterListList();
        Set<String> kesy = new HashSet<>(filterListList.size());
        filterListList.forEach((v) -> kesy.add(this.buildCacheKey(v.getMode(), v.getTarget().getType(), v.getTarget().getGuid())));
        this.redisCache.remove(kesy);
    }

}
