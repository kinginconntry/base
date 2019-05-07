package com.needto.services.config;

import com.needto.common.entity.Target;
import com.needto.dao.models.BaseEntity;

/**
 * @author Administrator
 */
public class Config extends BaseEntity {
    public static final String TABLE = "_config";

    /**
     * 配置所属
     */
    public Target belongTo;

    /**
     * 归属分类
     * @see ConfigCat
     */
    public String catId;

    /**
     * 字典外部显示值
     */
    private String name;

    /**
     * 系统设置值
     */
    private String key;

    /**
     * 描述
     */
    private String desc;

    /**
     * 字典实际的值（字符串，json对象，数组等）
     */
    private Object value;

    /**
     * 是否系统预置
     */
    private boolean preset;

    /**
     * 是否启用
     */
    private boolean able;

    /**
     * 是否显示
     */
    private boolean show;

    /**
     * 排序号
     */
    private int order;

    public Target getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Target belongTo) {
        this.belongTo = belongTo;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public boolean isPreset() {
        return preset;
    }

    public void setPreset(boolean preset) {
        this.preset = preset;
    }

    public boolean isAble() {
        return able;
    }

    public void setAble(boolean able) {
        this.able = able;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
