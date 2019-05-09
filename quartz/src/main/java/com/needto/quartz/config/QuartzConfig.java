package com.needto.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PreDestroy;

/**
 * @author Administrator
 */
@Configuration
public class QuartzConfig {

    private static final Logger LOG = LoggerFactory.getLogger(QuartzConfig.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private Environment environment;

    @PreDestroy
    public void destroy() {
        boolean performShutdown = Boolean.valueOf(environment.getProperty("quartz.performShutdown", "false"));
        boolean waitOnShutdown = Boolean.valueOf(environment.getProperty("quartz.waitOnShutdown", "false"));
        if (!performShutdown) {
            return;
        }
        try {
            if (scheduler != null) {
                scheduler.shutdown(waitOnShutdown);
            }
        } catch (Exception e) {
            LOG.error("Quartz定时任务调度器关闭失败： " + e.toString());
            e.printStackTrace();
        }
        LOG.info("Quartz定时任务调度器成功关闭");
    }
    /**
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name="scheduler")
    public Scheduler scheduler() throws SchedulerException {
        // 获取调度器
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        /**
         * 是否自动加启动schduler
         */
        Boolean startOnLoad = Boolean.valueOf(environment.getProperty("quartz.startOnLoad", "false"));
        /**
         * 加载初始化延迟时间
         */
        Integer startDelay = Integer.valueOf(environment.getProperty("quartz.startDelay", "5"));

        if(startOnLoad){
            if (startDelay <= 0){
                scheduler.start();
                LOG.info("Quartz定时任务调度器正在启动中...!");
            }else{
                scheduler.startDelayed(startDelay);
                LOG.info("Quartz定时任务调度器将在" + startDelay + "秒后启动!");
            }
        }else{
            LOG.info("Quartz定时任务调度器没有启动，请先启动定时任务调度器");
        }
        return scheduler;
    }

}
