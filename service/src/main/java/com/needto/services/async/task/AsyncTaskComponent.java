package com.needto.services.async.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * @author Administrator
 * 异步任务池
 */
@Component
public class AsyncTaskComponent {

    protected final static Logger LOG = LoggerFactory.getLogger(AsyncTaskComponent.class);

    private ExecutorService executorService;

    @PostConstruct
    private void init(){
        LOG.info("start asyncServiceExecutor");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<>(), threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public <T> void startTask(Supplier<T> supplier){
        executorService.execute(supplier::get);
    }

}