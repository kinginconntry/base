package com.needto.quartz.model;

/**
 * @author Administrator
 * quartz触发器
 */
public class QuartzTrigger {
    public String schedulerName;
    public String jobName;
    public String jobGroup;
    public String name;
    public String group;
    public String desc;
    public long nextFireTime;
    public long prevFireTime;
    public int priority;
    public String triggerState;
    public String triggerType;
    public long startTime;
    public long endTime;
    public String calendarName;
    public int misfireInstr;

    public static void builder(QuartzTrigger quartzTrigger, Dict dict){

    }

    public String getSchedulerName() {
        return schedulerName;
    }

    public void setSchedulerName(String schedulerName) {
        this.schedulerName = schedulerName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
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

    public long getNextFireTime() {
        return nextFireTime;
    }

    public void setNextFireTime(long nextFireTime) {
        this.nextFireTime = nextFireTime;
    }

    public long getPrevFireTime() {
        return prevFireTime;
    }

    public void setPrevFireTime(long prevFireTime) {
        this.prevFireTime = prevFireTime;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getTriggerState() {
        return triggerState;
    }

    public void setTriggerState(String triggerState) {
        this.triggerState = triggerState;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public int getMisfireInstr() {
        return misfireInstr;
    }

    public void setMisfireInstr(int misfireInstr) {
        this.misfireInstr = misfireInstr;
    }
}
