package com.needto.discount.entity;

import java.math.BigDecimal;

/**
 * @author Administrator
 * 单条折扣
 */
public class DiscountUnitResult {

    /**
     * 打折是否成功
     */
    public boolean success;

    /**
     * 折扣钱
     */
    public BigDecimal fee;

    /**
     * 折扣编码
     */
    public String code;

    /**
     * 失败码
     */
    public String error;

    /**
     * 失败消息
     */
    public String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
