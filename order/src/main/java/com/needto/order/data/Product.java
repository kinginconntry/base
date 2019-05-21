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
     * 产品单价，必须
     */
    private long price;
    /**
     * 价格单位：默认元
     */
    private String unit;
    /**
     * 产品数量，必须
     */
    private int number;

    private Discount discount;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Discount getDiscount() {
        return discount;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }
}
