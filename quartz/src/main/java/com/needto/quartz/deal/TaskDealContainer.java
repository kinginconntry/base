package com.needto.quartz.deal;

import com.needto.common.service.ThingContainerService;
import com.needto.quartz.entity.TaskData;
import com.needto.quartz.entity.TaskEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class TaskDealContainer extends ThingContainerService<TaskDeal> {

    private static final Log log = LogFactory.getLog(TaskDealContainer.class);



    @EventListener
    public void deal(TaskEvent taskEvent){
        TaskData taskData = taskEvent.getTaskData();
        TaskDeal taskDeal = this.get(taskEvent.getDeal());
        if(taskDeal != null){
            taskDeal.deal(taskData);
        }else{
            // 没有找到执行器，执行失败
            log.info("没有找到执行器 " + taskEvent.getDeal()+ "，执行失败");
        }
    }

    @Override
    protected Class<TaskDeal> getThingClass() {
        return TaskDeal.class;
    }
}
