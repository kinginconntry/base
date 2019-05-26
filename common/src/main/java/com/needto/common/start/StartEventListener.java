package com.needto.common.start;


import com.needto.common.context.SpringEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartEventListener {

    private static final Logger LOG = LoggerFactory.getLogger(StartEventListener.class);
    /**
     * 更新上下文事件
     * @param contextRefreshedEvent
     */
    @EventListener
    private void refreshContext(ContextRefreshedEvent contextRefreshedEvent){
        SpringEnv.setApplicationContext(contextRefreshedEvent.getApplicationContext());
        LOG.debug("初始化Spring全局上下文事件");
    }
}
