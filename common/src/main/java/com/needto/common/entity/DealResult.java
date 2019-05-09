package com.needto.common.entity;

/**
 * @author Administrator
 * 内部处理结果
 */
public class DealResult{

    public boolean success;

    public Object data;

    public Result getResult(){
        Result<Object> res = new Result<>();
        res.setData(this.data);
        res.setSuccess(this.success);
        return res;
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
