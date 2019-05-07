package com.needto.common.context;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * spring全局事件监听器
 * @author Administrator
 */
@Component
public class GlobalEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalEventListener.class);

    @Autowired
    private IClientCache iClientCache;

    /**
     * 更新上下文事件
     * @param contextRefreshedEvent
     */
    @EventListener
    private void refreshContext(ContextRefreshedEvent contextRefreshedEvent){
        GlobalEnv.setApplicationContext(contextRefreshedEvent.getApplicationContext());
        GlobalEnv.setEnvironment(contextRefreshedEvent.getApplicationContext().getEnvironment());
        LOG.debug("初始化全局上下文事件");
    }

    @EventListener
    private void removeClientFinger(RemoveClientFingerEvent removeClientFingerEvent){
        iClientCache.remove(removeClientFingerEvent.getClient());
        LOG.debug("移除客户端指纹信息");
    }
}
