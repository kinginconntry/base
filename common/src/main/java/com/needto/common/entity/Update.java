package com.needto.common.entity;

/**
 * @author Administrator
 * 字段更新信息
 */
public class Update {

    /**
     * 更新字段
     */
    public String field;

    /**
     * 更新值
     */
    public Object value;

    public Update() {
    }

    public Update(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
