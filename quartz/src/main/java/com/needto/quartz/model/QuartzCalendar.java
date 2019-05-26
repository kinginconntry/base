package com.needto.quartz.model;

import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 * 触发器日历
 */
public class QuartzCalendar {
    public String schedulerName;
    public String name;
    public Object calendar;

    public static QuartzCalendar builder(Dict dict){
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

    public Object getCalendar() {
        return calendar;
    }

    public void setCalendar(Object calendar) {
        this.calendar = calendar;
    }
}
