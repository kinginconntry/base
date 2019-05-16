package com.needto.quartz.entity;

public class Key {
    /**
     * 分组
     */
    public String group;
    /**
     * 名称，唯一且不可为空
     */
    public String name;

    public Key(String group, String name) {
        this.group = group;
        this.name = name;
    }

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
}
