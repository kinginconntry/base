package com.needto.quartz.entity;

import org.springframework.context.ApplicationEvent;

public class TaskEvent extends ApplicationEvent {

    public TaskData taskData;
    public String deal;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public TaskEvent(Object source, TaskData taskData, String deal) {
        super(source);
        this.taskData = taskData;
        this.deal = deal;
    }

    public TaskData getTaskData() {
        return taskData;
    }

    public void setTaskData(TaskData taskData) {
        this.taskData = taskData;
    }

    public String getDeal() {
        return deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }
}
