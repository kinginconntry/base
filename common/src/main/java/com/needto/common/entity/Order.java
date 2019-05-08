package com.needto.common.entity;

/**
 * @author Administrator
 */
public class Order {

    /**
     * 排序字段
     */
    public String field;
    /**
     * 排序顺序（大于等于0为正序，否则为逆序）
     */
    public int order;

    public Order() {
    }

    public Order(String field, int order) {
        this.field = field;
        this.order = order;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
