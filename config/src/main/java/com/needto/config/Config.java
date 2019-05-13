package com.needto.config;

import com.needto.common.entity.Target;
import com.needto.common.inter.IOrder;
import com.needto.dao.models.UserEntity;

/**
 * @author Administrator
 */
public class Config extends UserEntity implements IOrder {
    public static final String TABLE = "_config";

    /**
     * 配置所属
     */
    public Target belongto;

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
     * 是否启用
     */
    private boolean enable;

    /**
     * 排序号
     */
    private int order;

    public Target getBelongto() {
        return belongto;
    }

    public void setBelongto(Target belongto) {
        this.belongto = belongto;
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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
