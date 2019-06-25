package com.needto.group;

import com.needto.dao.models.BaseEntity;
import com.needto.tool.entity.Dict;
import com.needto.tool.inter.Itree;

/**
 * 分组实体
 * @author Administrator
 */
public class Group extends BaseEntity implements Itree {

    /**
     * 父节点id
     */
    private String pid;

    /**
     * 分组名
     */
    private String name;

    /**
     * 分组描述
     */
    private String desc;

    /**
     * 图标
     */
    private String icon;

    /**
     * 数据
     */
    private Dict data;

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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }

    @Override
    public String getCode() {
        return this.id;
    }

    @Override
    public String getPcode() {
        return this.pid;
    }
}
