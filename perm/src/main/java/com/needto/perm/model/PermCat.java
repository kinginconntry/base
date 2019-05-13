package com.needto.perm.model;

import com.needto.dao.models.UserEntity;

public class PermCat extends UserEntity {

    public static final String TABLE = "_permcat";

    /**
     * 名称
     */
    public String name;

    /**
     * 图标
     */
    public String icon;

    /**
     * 描述
     */
    public String desc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
