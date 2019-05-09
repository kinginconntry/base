package com.needto.services.user.resourceperm;


import com.needto.dao.models.UserEntity;

import java.util.List;


/**
 * @author Administrator
 * 用户角色
 */
public class Role extends UserEntity {

    public static final String TABLE = "_role";

    /**
     * 主用户id
     */
    private String owner;

    /**
     * 角色名
     */
    public String name;

    /**
     * 角色描述
     */
    public String desc;

    /**
     * 角色图标
     */
    public String icon;

    /**
     * 权限码集合（功能权限）
     */
    public List<String> perms;

    /**
     * 数据控制权限id
     */
    public List<String> dataperms;

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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getPerms() {
        return perms;
    }

    public void setPerms(List<String> perms) {
        this.perms = perms;
    }

    public List<String> getDataperms() {
        return dataperms;
    }

    public void setDataperms(List<String> dataperms) {
        this.dataperms = dataperms;
    }
}
