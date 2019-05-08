package com.needto.common.entity;


/**
 * 字段过滤
 * @author Administrator
 */
public class Filter {

    /**
     * 字段名
     */
    public String field;

    /**
     * 操作
     */
    public String op;

    /**
     * 值
     */
    public Object value;

    public Filter() {
    }

    public Filter(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public Filter(String field, String op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
