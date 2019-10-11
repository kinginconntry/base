package com.needto.common.start;


import com.needto.common.context.SpringEnv;
import com.needto.common.entity.SystemState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.*;

@Configuration
public class StartEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(StartEventListener.class);
    /**
     * 更新上下文事件
     * @param contextRefreshedEvent
     */
    @EventListener(classes = {ContextRefreshedEvent.class})
    private void refreshContext(ContextRefreshedEvent contextRefreshedEvent){
        SpringEnv.setApplicationContext(contextRefreshedEvent.getApplicationContext());
        LOG.debug("Spring全局上下文事件：容器初始化完成");
        SpringEnv.setStatus(SystemState.RUNNING);
    }

    @EventListener(classes = {ContextStartedEvent.class})
    private void contextStartedEvent(ContextStartedEvent contextStartedEvent){
        LOG.debug("Spring全局上下文事件：容器开始初始化");
        SpringEnv.setStatus(SystemState.INITIATING);
    }

    @EventListener(classes = {ContextStoppedEvent.class})
    private void contextStoppedEvent(ContextStoppedEvent contextStoppedEvent){
        LOG.debug("Spring全局上下文事件：容器停止");
        SpringEnv.setStatus(SystemState.STOP);
    }

    @EventListener(classes = {ContextClosedEvent.class})
    private void contextClosedEvent(ContextClosedEvent contextClosedEvent){
        LOG.debug("Spring全局上下文事件：容器关闭");
        SpringEnv.setStatus(SystemState.NO);
    }
}
