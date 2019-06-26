package com.needto.order.data;

import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 */
public class Product {
    /**
     * 产品编号:通过该key可以找到产品相关信息
     */
    private String key;
    /**
     * 产品数量，必须
     */
    private int number;

    /**
     * 价格
     */
    private long price;

    /**
     * 折扣服务编码
     */
    public String code;

    /**
     * 折扣配置
     */
    public Dict config;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
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
