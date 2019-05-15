package com.needto.quartz.model;

import com.needto.common.entity.Dict;

/**
 * @author Administrator
 * quartz调度器
 */
public class QuartzScheduler {
    public String name;
    public String instanceName;
    public long lastCheckinTime;
    public long checkinInterval;

    public static QuartzScheduler builder(Dict dict){
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public long getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(long lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public long getCheckinInterval() {
        return checkinInterval;
    }

    public void setCheckinInterval(long checkinInterval) {
        this.checkinInterval = checkinInterval;
    }
}
