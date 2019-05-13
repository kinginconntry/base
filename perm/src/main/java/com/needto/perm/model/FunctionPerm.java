package com.needto.perm.model;

import com.needto.dao.models.UserEntity;

/**
 * @author Administrator
 * 功能权限：权限节点
 */
public class FunctionPerm extends UserEntity{

    public static final String TABLE = "_funcperm";

    /**
     * 分类id
     */
    public String catId;

    /**
     * 权限码
     */
    public String perm;

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

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getPerm() {
        return perm;
    }

    public void setPerm(String perm) {
        this.perm = perm;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
