package com.needto.dao.common;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 字段过滤
 * @author Administrator
 */
public class FieldFilter {

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

    public FieldFilter() {
    }

    public FieldFilter(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public FieldFilter(String field, String op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public static List<FieldFilter> single(String field, String op, Object value){
        return Lists.newArrayList(new FieldFilter(field, op, value));
    }

    public static List<FieldFilter> single(String field, Object value){
        return Lists.newArrayList(new FieldFilter(field, value));
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
