package com.needto.quartz.entity;

public class TaskData {
    public Dict data;
    public String jobGroup;
    public String jobName;
    public String triggerGroup;
    public String triggerName;

    public Dict getData() {
        return data;
    }

    public void setData(Dict data) {
        this.data = data;
    }

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

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

}
