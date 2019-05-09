package com.needto.quartz.service;

import com.needto.common.utils.Assert;
import com.needto.quartz.data.Constants;
import com.needto.quartz.deal.TaskDealContainer;
import com.needto.quartz.entity.CalendarData;
import com.needto.quartz.entity.JobData;
import com.needto.quartz.entity.Key;
import com.needto.quartz.entity.TriggerData;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * Created by Administrator on 2018/4/23 0023.
 */

@Component
public class ScheduleManager implements IScheduleManager {

    private static final Log log = LogFactory.getLog(ScheduleManager.class);

    @Resource
    private Scheduler scheduler;

    @Autowired
    private TaskDealContainer taskDealContainer;

    /**
     * 只增加一个任务信息，不添加触发器
     * @param jobData 任务执行师的数据
     * @return
     */
    @Override
    public <T extends Job> boolean addJob(JobData jobData, Class<T> jobClass) {

        //设置任务重复提交标识
        String name = jobData.getName();
        String group = jobData.getGroup();
        JobKey jobKey = new JobKey(name, group);

        try {
            Assert.validateCondition(!jobData.isReplace() && scheduler.checkExists(jobKey), "EXIST_JOB", "");
            Assert.validateCondition(taskDealContainer.contain(jobData.getDeal()), "NO_SUPPORT_DEAL", "");
            // 定义任务详细
            JobDetailImpl jobDetail = new JobDetailImpl();
            jobDetail.setJobClass(jobClass);
            //设置任务属性
            JobDataMap jobDataMap=new JobDataMap();
            jobDataMap.putAll(jobData.getData());
            jobDataMap.put(Constants.DEAL_KEY, jobData.getDeal());
            jobDetail.setJobDataMap(jobDataMap);
            jobDetail.setDescription(jobData.getDesc());
            jobDetail.setName(name);
            jobDetail.setGroup(group);
            jobDetail.setKey(jobKey);
            jobDetail.setDurability(jobData.isDurable());
            jobDetail.setRequestsRecovery(true);
            scheduler.addJob(jobDetail, jobData.isReplace(), true);
            log.info(String.format("创建任务，任务分组为%s，任务名为%s", jobDetail.getGroup(), jobDetail.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 暂停任务
     *
     */
    @Override
    public boolean pauseJob(Key key) {
        Assert.validateNull(key);
        Assert.validateStringEmpty(key.getName());
        try {
            scheduler.pauseJob(new JobKey(key.getName(), key.getGroup()));
            log.info(String.format("暂停任务，任务分组为%s，任务名为%s", key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 移除任务
     *
     */
    @Override
    public boolean removeJob(Key key){
        Assert.validateNull(key);
        Assert.validateStringEmpty(key.getName());
        try {
            scheduler.deleteJob(new JobKey(key.getName(), key.getGroup()));
            log.info(String.format("移除任务，任务分组为%s，任务名为%s", key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 恢复任务
     *
     */
    @Override
    public boolean resumeJob(Key key){
        Assert.validateNull(key);
        try {
            scheduler.resumeJob(new JobKey(key.getName(), key.getGroup()));
            log.info(String.format("恢复任务，任务分组为%s，任务名为%s", key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 直接为某个key和group组成的任务添加一个触发器
     * @param triggerData 触发器信息
     * @return
     */
    @Override
    public boolean addTrigger(TriggerData triggerData) {
        Assert.validateNull(triggerData);
        Assert.validateNull(triggerData.getTriggerType());
        Assert.validateNull(triggerData.getConfig());
        Trigger trigger = triggerData.getTriggerType().getTrigger(triggerData);
        try {
            Assert.validateCondition(!StringUtils.isEmpty(triggerData.getCalanderName()) && scheduler.getCalendar(triggerData.getCalanderName()) == null, "INVALID_CALENDAR", "");
            scheduler.scheduleJob(trigger);
            log.info(String.format("添加触发器，任务分组为%s，任务名为%s，触发器分组为%s，触发器名为%s", triggerData.getJobGroup(), triggerData.getJobName(), triggerData.getGroup(), triggerData.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 暂停触发器
     * @return
     */
    @Override
    public boolean pauseTrigger(Key key) {
        Assert.validateNull(key);
        Assert.validateStringEmpty(key.getName());
        try {
            scheduler.pauseTrigger(new TriggerKey(key.getName(), key.getGroup()));
            log.info(String.format("暂停触发器，触发器分组为%s，触发器名为%s", key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    /**
     * 恢复触发器
     * @return
     */
    @Override
    public boolean resumeTrigger(Key key) {
        Assert.validateNull(key);
        Assert.validateStringEmpty(key.getName());
        try {
            scheduler.resumeTrigger(new TriggerKey(key.getName(), key.getGroup()));
            log.info(String.format("恢复触发器，触发器分组为%s，触发器名为%s",key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean removeTrigger(Key key) {
        Assert.validateNull(key);
        Assert.validateStringEmpty(key.getName());
        try {
            scheduler.unscheduleJob(new TriggerKey(key.getName(), key.getGroup()));
            log.info(String.format("移除触发器，触发器分组为%s，触发器名为%s", key.getGroup(), key.getName()));
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean addCanlendar(CalendarData calendarData) {
        Assert.validateNull(calendarData);
        Assert.validateStringEmpty(calendarData.getName(), "EMPTY_NAME", "");
        try {
            scheduler.addCalendar(calendarData.getName(), calendarData.getCalendar(), calendarData.isReplace(), calendarData.isUpdateTriggers());
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeCalendar(String name) {
        Assert.validateStringEmpty(name);
        try {
            scheduler.deleteCalendar(name);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

}
