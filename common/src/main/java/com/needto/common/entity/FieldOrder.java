package com.needto.common.entity;

/**
 * @author Administrator
 */
public class FieldOrder {

    /**
     * 排序字段
     */
    public String field;
    /**
     * 排序顺序（大于等于0为正序，否则为逆序）
     */
    public int order;

    public FieldOrder() {
    }

    public FieldOrder(String field, int order) {
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
