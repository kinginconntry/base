package com.needto.quartz.entity;


import java.util.Map;

/**
 * @author Administrator
 */
public class JobData{

    /**
     * 分组
     */
    public String group;

    /**
     * 所属
     */
    public String name;

    /**
     * 任务描述
     */
    private String desc;

    /**
     * 是否持久化
     */
    private boolean durable;

    /**
     * 是否可替换
     */
    private boolean replace;

    private String deal;

    private Map<String, Object> data;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
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

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }
}
