package com.needto.common.services.async.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 通用异步执行器，由于spring的@Async注解在同一个类中的方法相互调用不会异步，因此可用这个类来进行异步。
 * @author Administrator
 */
@Service
public class AsyncTaskExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(AsyncTaskExecutor.class);

    @Async
    public void execute(String jobName, Runnable r) {
        LOG.debug("try executing {} async", jobName);
        try {
            r.run();
            LOG.debug("异步任务执行完成");
        } catch (Exception e) {
            LOG.error("异步执行任务{}错误", jobName, e);
        }
    }
}
