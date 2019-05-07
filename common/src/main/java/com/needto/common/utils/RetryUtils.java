package com.needto.common.utils;

import com.needto.common.entity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

/**
 * 一般的执行、错误、重试逻辑的封装
 * 经常有这种执行、错误重试几次的常规逻辑，用一个工具方法封装过程
 * @author ylx
 */
public class RetryUtils {
    private static final Logger LOG = LoggerFactory.getLogger(RetryUtils.class);

    private static final int MAX_RETRY_COUNT = 10;

    /**
     * 执行一个传入的job，如果发生异常，则在指定的间隔时间后重试执行指定的次数
     * @param job 要执行的job
     * @param jobDesc job说明，主要用于记日志
     * @param retryCount 重试次数,包含第一次，不能小于1，不能大于10
     * @param waitTime 重试之间的间隔时间，毫秒单位，不能小于0
     * @param <R> 返回值类型
     * @return 执行结果数据，如果重试次数超过仍发生异常,返回结果error=retryCount,
     *                 如果等待线程被终端，返回结果error=interrupted
     */
    public static <R> Result<R> run(int retryCount, long waitTime, String jobDesc, Callable<R> job) {
        return run(retryCount, waitTime, jobDesc, job, (r) -> true);
    }

    public static <R> Result<R> run(int retryCount, long waitTime, Callable<R> job) {
        return run(retryCount, waitTime, "", job, (r) -> true);
    }

    /**
     * 执行一个传入的job，如果发生异常或者判断返回结果不成功，则在指定的间隔时间后重试执行指定的次数
     * @param job 要执行的job
     * @param jobDesc job说明，主要用于记日志
     * @param predicate 执行结果数据是否成功的判断逻辑
     * @param retryCount 重试次数,包含第一次
     * @param waitTime 重试之间的间隔时间
     * @param <R> 返回值类型
     * @return 执行结果数据，如果重试次数超过仍发生异常,返回结果error=retryCount,
     *                 如果等待线程被终端，返回结果error=interrupted
     */
    public static <R> Result<R> run(int retryCount, long waitTime, String jobDesc, Callable<R> job, Predicate<R> predicate) {
        Objects.requireNonNull(job);
        if (retryCount < 1 || retryCount > MAX_RETRY_COUNT) {
            throw new IllegalArgumentException("retryCount");
        }
        if (waitTime <= 0) {
            throw new IllegalArgumentException("waitTime");
        }

        int n = 0;
        Exception ex = null;
        do {
            n++;
            try {
                R data = job.call();
                if (predicate.test(data)) {
                    return Result.forSuccess(data);
                }
                Thread.sleep(waitTime);
            } catch (InterruptedException ie) {
                LOG.error("{} retry {} times was interrupted", jobDesc, n, ie);
                return Result.forError("interrupted", ie.getMessage());
            } catch (Exception e) {
                ex = e;
            }
        } while(n < retryCount);

        LOG.error("{} retry {} times still error", jobDesc, n, ex);
        return Result.forError("retryCount", null);
    }
}
