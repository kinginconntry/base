package com.needto.order.data;

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
}
