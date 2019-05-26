package com.needto.quartz.entity;


import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 */
public class TriggerData {

    public String jobGroup;

    public String jobName;

    /**
     * 分组
     */
    public String group;

    /**
     * 名称
     */
    public String name;

    /**
     * 触发器的描述
     */
    public String desc = "";

    /**
     * 触发器类型
     */
    public TriggerType triggerType;

    /**
     * 触发器配置
     */
    public Dict config;

    /**
     * 关联日历
     */
    public String calanderName;

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public TriggerType getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(TriggerType triggerType) {
        this.triggerType = triggerType;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }

    public String getCalanderName() {
        return calanderName;
    }

    public void setCalanderName(String calanderName) {
        this.calanderName = calanderName;
    }
}
