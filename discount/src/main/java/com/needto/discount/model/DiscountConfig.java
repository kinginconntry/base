package com.needto.discount.model;

import com.needto.common.entity.Target;
import com.needto.dao.models.TargetEntity;
import com.needto.tool.entity.Dict;

public class DiscountConfig extends TargetEntity {

    public static final String TABLE = "_discountconfig";

    public Target belong;

    public String key;

    public Dict config;

    public Target getBelong() {
        return belong;
    }

    public void setBelong(Target belong) {
        this.belong = belong;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }
}
