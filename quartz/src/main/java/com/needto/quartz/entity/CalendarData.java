package com.needto.quartz.entity;

import org.quartz.Calendar;

/**
 * @author Administrator
 * 日历配置
 */
public class CalendarData {

    private String name;
    private Calendar calendar;
    private boolean replace;
    private boolean updateTriggers;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }

    public boolean isUpdateTriggers() {
        return updateTriggers;
    }

    public void setUpdateTriggers(boolean updateTriggers) {
        this.updateTriggers = updateTriggers;
    }
}
