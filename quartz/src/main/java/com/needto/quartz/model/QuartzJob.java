package com.needto.quartz.model;

import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * quartz任务
 */
public class QuartzJob {
    public String schedulerName;
    public String name;
    public String group;
    public String desc;
    public int durable;
    public int nonConcurrent;
    public int updateData;
    public int requestsRecovery;
    public Object data;

    public static QuartzJob builder(Dict dict){
        return null;
    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDurable() {
        return durable;
    }

    public void setDurable(int durable) {
        this.durable = durable;
    }

    public int getNonConcurrent() {
        return nonConcurrent;
    }

    public void setNonConcurrent(int nonConcurrent) {
        this.nonConcurrent = nonConcurrent;
    }

    public int getUpdateData() {
        return updateData;
    }

    public void setUpdateData(int updateData) {
        this.updateData = updateData;
    }

    public int getRequestsRecovery() {
        return requestsRecovery;
    }

    public void setRequestsRecovery(int requestsRecovery) {
        this.requestsRecovery = requestsRecovery;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
