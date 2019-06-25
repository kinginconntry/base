package com.needto.perm.model;



import com.needto.dao.models.BaseEntity;

import java.util.List;


/**
 * @author Administrator
 * 用户角色
 */
public class Role extends BaseEntity {

    public static final String TABLE = "_role";

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
     * 权限id集合（功能权限）
     */
    public List<String> funcperms;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<String> getFunctionPerms() {
        return funcperms;
    }

    public void setFunctionPerms(List<String> perms) {
        this.funcperms = perms;
    }

    public List<String> getDataperms() {
        return dataperms;
    }

    public void setDataperms(List<String> dataperms) {
        this.dataperms = dataperms;
    }
}
