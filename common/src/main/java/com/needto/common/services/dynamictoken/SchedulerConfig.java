package com.needto.common.services.dynamictoken;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Resource
    private DynamicSignService dynamicSignService;

    @Scheduled(cron = "0 0 0/2 * * ?") // 每两个小时更新一次
    public void schedulerUpdateDynamicSign() {
        dynamicSignService.updateSign();
    }
}
