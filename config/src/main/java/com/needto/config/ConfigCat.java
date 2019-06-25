package com.needto.config;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;
import com.needto.tool.inter.IOrder;

/**
 * @author Administrator
 */
public class ConfigCat extends BaseEntity implements IOrder {

    public static final String TABLE = "_configCat";

    /**
     * 名称
     */
    public String name;

    /**
     * 描述
     */
    public String desc;

    /**
     * 序号
     */
    public int order;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
