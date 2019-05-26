package com.needto.quartz.deal;

import com.needto.quartz.entity.TaskData;
import com.needto.tool.inter.Thing;

/**
 * @author Administrator
 */
public interface TaskDeal extends Thing {

    /**
     * @param data
     */
    void deal(TaskData data);
}
