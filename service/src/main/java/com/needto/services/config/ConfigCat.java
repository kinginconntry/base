package com.needto.services.config;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;

/**
 * @author Administrator
 */
public class ConfigCat extends BaseEntity {

    public static final String TABLE = "_configCat";

    public Target belongto;

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

    public Target getBelongto() {
        return belongto;
    }

    public void setBelongto(Target belongto) {
        this.belongto = belongto;
    }

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

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
