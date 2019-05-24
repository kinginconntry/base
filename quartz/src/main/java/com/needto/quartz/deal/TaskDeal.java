package com.needto.quartz.deal;

import com.needto.quartz.entity.TaskData;

/**
 * @author Administrator
 */
public interface TaskDeal extends Thing {

    /**
     * @param data
     */
    void deal(TaskData data);
}
