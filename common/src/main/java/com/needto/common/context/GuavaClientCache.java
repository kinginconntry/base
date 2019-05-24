package com.needto.common.context;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.needto.common.entity.ClientType;
import com.needto.common.entity.Target;
import com.needto.common.inter.IClientCache;
import com.needto.tool.entity.Dict;
import com.needto.tool.exception.LogicException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 客户端缓存，基于guava 缓存
 * @author Administrator
 */
@Component
public class GuavaClientCache implements IClientCache {

    private static final Logger LOG = LoggerFactory.getLogger(GuavaClientCache.class);


    /**
     * 基于token的客户端指纹缓存，需要进行登录或已有用户信息
     * 作可刷新缓存
     */
    private Cache<String, Dict> tokenClientFingerCache;

    /**
     * 访客缓存
     * 作可刷新缓存
     */
    private Cache<String, Dict> webClientFingerCache;

    @Autowired
    private Environment environment;

    /**
     * 非授权客户端是否新建空间
     */
    private boolean noAuthCreate;

    /**
     *
     */
    private static final Dict DEFAULT_NO_AUTH_CACHE = new Dict();

    @PostConstruct
    public void init(){
        // 默认30天，43200分钟
        String tokenExpire = environment.getProperty("client.expire.token", "43200");
        String tokenMax = environment.getProperty("client.max.token", "10000");
        tokenClientFingerCache = CacheBuilder.newBuilder()
                .maximumSize(Integer.valueOf(tokenMax))
                .expireAfterAccess(Integer.valueOf(tokenExpire), TimeUnit.MINUTES)
                .build();

        // 默认30分钟
        String webExpire = environment.getProperty("client.expire.web", "30");
        String webMax = environment.getProperty("client.max.web", "10000");
        webClientFingerCache = CacheBuilder.newBuilder()
                .maximumSize(Integer.valueOf(webMax))
                .expireAfterAccess(Integer.valueOf(webExpire), TimeUnit.MINUTES)
                .build();

        noAuthCreate = Boolean.valueOf(environment.getProperty("client.noauth.create", "true"));
        LOG.debug("客户端缓存容器初始化，client.expire.token {}，client.max.token {}，client.expire.web {}，client.max.web {}", tokenExpire, tokenMax, webExpire, webMax);
    }



    /**
     * 设置客户端指纹
     * @param client
     * @throws ExecutionException
     */
    @Override
    public Target refresh(Target client) {
        String key = getGuid(client);
        Dict dict = null;
        try {
            if(ClientType.NO_AUTH.name().equals(client.getType())){
                if(noAuthCreate){
                    dict = webClientFingerCache.get(key, Dict::new);
                }else{
                    dict = webClientFingerCache.get(key, () -> DEFAULT_NO_AUTH_CACHE);
                }
            }else{
                dict = tokenClientFingerCache.get(key, Dict::new);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(dict == null){
            // 未知的客户端
            throw new LogicException("UNKNOW_CLIENT", "");
        }
        LOG.debug("刷新客户端指纹信息");
        return client;
    }


    /**
     * 获取客户端的信息对象
     * @param client
     * @return
     */
    @Override
    public Dict get(Target client){
        String key = getGuid(client);
        if(ClientType.NO_AUTH.name().equals(client.getType())){
            return webClientFingerCache.getIfPresent(key);
        }else{
            return tokenClientFingerCache.getIfPresent(key);
        }
    }

    /**
     * 主动移除客户端信息（被动等缓存过期）
     * @param client
     */
    @Override
    public void remove(Target client){
        String key = getGuid(client);
        if(ClientType.NO_AUTH.name().equals(client.getType())){
            webClientFingerCache.invalidate(key);
        }else{
            tokenClientFingerCache.invalidate(key);
        }
        LOG.debug("移除客户端指纹信息");
    }
}
