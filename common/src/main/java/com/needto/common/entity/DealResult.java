package com.needto.common.entity;

/**
 * @author Administrator
 * 外部处理结果
 */
public class DealResult extends Dict {

    public boolean success;

    public DealResult() {
    }

    public DealResult(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
