package com.needto.services.async.api;


/**
 * @author Administrator
 */
public class AsyncResult<T>{

    /**
     * 结果数据
     */
    private T value;

    /**
     * 结果完成度：%
     */
    private float percent;

    /**
     * 估计还需要耗时: 秒
     */
    private Long estimatedTime;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        this.percent = 100;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    /**
     * 结果进度条最多能达到百分之99.99，只有存储数据后才能达到100%
     * @param inc
     */
    public void incPercent(int inc){
        percent += inc;
        if(percent < 0){
            percent = 0;
        }else if(percent > 100){
            percent = 99.99f;
        }
    }

    /**
     * 异步任务是否完成
     * @return
     */
    public boolean isSuccess(){
        return this.percent == 100;
    }

    public Long getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Long estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
}
