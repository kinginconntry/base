package com.needto.services.user.resourceperm;

import com.needto.common.entity.Itree;
import com.needto.dao.models.BaseEntity;

/**
 * @author Administrator
 * 资源定义：权限节点，系统定义出来
 */
public class Permission extends BaseEntity implements Itree {

    public static final String TABLE = "_permission";

    /**
     * 父级节点id
     */
    public String pid;

    /**
     * 节点权限码
     */
    public String perm;

    /**
     * 节点名称
     */
    public String name;

    /**
     * 节点图标
     */
    public String icon;

    /**
     * 节点描述
     */
    public String desc;

    @Override
    public String getCode() {
        return this.id;
    }

    @Override
    public String getPcode() {
        return this.pid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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
