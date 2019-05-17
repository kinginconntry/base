package com.needto.quartz.deal;

import com.needto.quartz.entity.TaskData;

/**
 * @author Administrator
 */
public interface TaskDeal {

    /**
     * @param data
     */
    void deal(TaskData data);

    String code();

    default String name(){ return ""; }

    default String desc(){ return ""; }
}
