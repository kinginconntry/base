package com.needto.common.entity;

import com.needto.common.inter.Itree;

/**
 * @author Administrator
 * 通用节点实现
 */
public class Tree<T> implements Itree {

    /**
     * 节点名
     */
    public String name;

    /**
     * 节点描述
     */
    public String desc;

    /**
     * 父节点code
     */
    public String pcode;

    /**
     * 节点code
     */
    public String code;

    /**
     * 节点图标
     */
    public String icon;

    /**
     * 节点数据
     */
    public T data;

    public Tree(String pcode, String code) {
        this.pcode = pcode;
        this.code = code;
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

    @Override
    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
