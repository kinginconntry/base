package com.needto.account.entity;

import com.needto.tool.entity.Dict;

public class Price {

    /**
     * 单价
     */
    public Long sum;

    /**
     * 最小购买数量
     */
    public Integer min;

    /**
     * 最大购买量
     */
    public Integer max;

    /**
     * 价格描述
     */
    public String desc;

    /**
     * 折扣
     */
    public String discount;

    /**
     * 折扣配置
     */
    public Dict discountConfig;

    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public Dict getDiscountConfig() {
        return discountConfig;
    }

    public void setDiscountConfig(Dict discountConfig) {
        this.discountConfig = discountConfig;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }
}
