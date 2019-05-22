package com.needto.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Administrator
 * 异步任务池组件
 */
public class AsyncTaskComponent {

    protected final static Logger LOG = LoggerFactory.getLogger(AsyncTaskComponent.class);

    private ExecutorService executorService;

    public AsyncTaskComponent(){
        this(0, Integer.MAX_VALUE, 60L, SECONDS, new SynchronousQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public AsyncTaskComponent(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler){
        LOG.info("start asyncServiceExecutor");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executorService = new ThreadPoolExecutor(corePoolSize, maximumPoolSize,
                keepAliveTime, unit,
                workQueue, threadFactory, handler);
    }

    public <T> void startTask(Supplier<T> supplier){
        executorService.execute(supplier::get);
    }

}