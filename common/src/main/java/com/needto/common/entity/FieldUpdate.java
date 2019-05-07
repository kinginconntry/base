package com.needto.common.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 字段更新信息
 */
public class FieldUpdate {

    /**
     * 更新字段
     */
    public String field;

    /**
     * 更新值
     */
    public Object value;

    public FieldUpdate() {
    }

    public FieldUpdate(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public static List<FieldUpdate> single(String field, Object value){
        List<FieldUpdate> updates = new ArrayList<>(1);
        updates.add(new FieldUpdate(field, value));
        return updates;
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
