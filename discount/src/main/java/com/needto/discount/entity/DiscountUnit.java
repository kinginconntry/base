package com.needto.discount.entity;

import com.needto.tool.entity.Dict;

import java.math.BigDecimal;

/**
 * @author Administrator
 * 单条折扣
 */
public class DiscountUnit {

    /**
     * 折扣服务编码
     */
    public String code;

    public Dict config;

    public BigDecimal fee;

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }
}
