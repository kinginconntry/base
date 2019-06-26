package com.needto.web.config;


import com.needto.web.context.WebEnv;
import com.needto.web.event.RemoveClientFingerEvent;
import com.needto.web.inter.IClientCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * spring全局事件监听器
 * @author Administrator
 */
@Component
public class WebEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(WebEventListener.class);

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 更新上下文事件
     * @param contextRefreshedEvent
     */
    @EventListener
    private void refreshContext(ContextRefreshedEvent contextRefreshedEvent){
        WebEnv.setApplicationContext(contextRefreshedEvent.getApplicationContext());
        WebEnv.setEnvironment(contextRefreshedEvent.getApplicationContext().getEnvironment());
        LOG.debug("初始化全局上下文事件");
    }

    @EventListener
    private void removeClientFinger(RemoveClientFingerEvent removeClientFingerEvent){
        applicationContext.getBean(IClientCache.class).remove(removeClientFingerEvent.getClient());
        LOG.debug("移除客户端指纹信息");
    }
}
