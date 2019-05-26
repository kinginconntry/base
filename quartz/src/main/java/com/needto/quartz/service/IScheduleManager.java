package com.needto.quartz.service;

import com.needto.quartz.entity.CalendarData;
import com.needto.quartz.entity.JobData;
import com.needto.quartz.entity.Key;
import com.needto.quartz.entity.TriggerData;
import com.needto.tool.utils.Assert;
import org.quartz.Job;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 * @author Administrator
 * @date 2018/4/23 0023
 * 任务调度的接口，包含任务的操作和触发器的操作
 */
public interface IScheduleManager {

    <T extends Job> boolean addJob(JobData data, Class<T> jobClass);
    boolean pauseJob(Key key);
    boolean removeJob(Key key);
    boolean resumeJob(Key key);

    default int pauseJob(List<Key> jobKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(jobKeys)){
            for(Key jobKey : jobKeys){
                if(this.pauseJob(jobKey)){
                    c += 1;
                }
            }
        }
        return c;
    }
    default int removeJob(List<Key> jobKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(jobKeys)){
            for(Key jobKey : jobKeys){
                if(this.removeJob(jobKey)){
                    c += 1;
                }
            }
        }
        return c;
    }
    default int resumeJob(List<Key> jobKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(jobKeys)){
            for(Key jobKey : jobKeys){
                if(this.resumeJob(jobKey)){
                    c += 1;
                }
            }
        }
        return c;
    }

    /**
     * trigger块
     */
    boolean addTrigger(TriggerData triggerData);
    boolean pauseTrigger(Key key);
    boolean resumeTrigger(Key key);
    boolean removeTrigger(Key key);

    boolean addCanlendar(CalendarData calendarData);

    boolean removeCalendar(String name);

    default int pauseTrigger(List<Key> triggerKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(triggerKeys)){
            for(Key triggerKey : triggerKeys){
                if(this.pauseTrigger(triggerKey)){
                    c += 1;
                }
            }
        }
        return c;
    }
    default int resumeTrigger(List<Key> triggerKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(triggerKeys)){
            for(Key triggerKey : triggerKeys){
                if(this.resumeTrigger(triggerKey)){
                    c += 1;
                }
            }
        }
        return c;
    }
    default int removeTrigger(List<Key> triggerKeys){
        int c = 0;
        if(!CollectionUtils.isEmpty(triggerKeys)){
            for(Key triggerKey : triggerKeys){
                if(this.removeTrigger(triggerKey)){
                    c += 1;
                }
            }
        }
        return c;
    }

    default <T extends Job> boolean addJobAndTrigger(JobData jobData, TriggerData triggerData, Class<T> jobClass) {

        if(!addJob(jobData, jobClass)){
            return false;
        }
        Assert.validateCondition(!addTrigger(triggerData), "CREATE_TRIGGER_FAILTURE", "");
        return true;
    }
}
