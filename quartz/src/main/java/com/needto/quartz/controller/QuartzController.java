package com.needto.quartz.controller;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Result;
import com.needto.quartz.entity.*;
import com.needto.quartz.model.QuartzCalendar;
import com.needto.quartz.model.QuartzJob;
import com.needto.quartz.model.QuartzScheduler;
import com.needto.quartz.model.QuartzTrigger;
import com.needto.quartz.service.QuartzManagerService;
import com.needto.quartz.service.ScheduleManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class QuartzController {

    @Autowired
    private QuartzManagerService quartzManagerService;

    @Autowired
    private ScheduleManager scheduleManager;

    /**
     * 查询调度器
     * @param query
     * @return
     */
    @RequestMapping(value={"/admin/quartz/scheduler/find", "/sys/quartz/scheduler/find"})
    @ResponseBody
    public Result<List<QuartzScheduler>> findScheduler(@RequestBody Query query){
        return Result.forSuccess(quartzManagerService.findScheduler(query));
    }

    /**
     * 查询任务
     * @param query
     * @return
     */
    @RequestMapping(value={"/admin/quartz/job/page", "/sys/quartz/job/page"})
    @ResponseBody
    public PageResult<List<QuartzJob>> pageJob(@RequestBody Query query){
        return quartzManagerService.pageJob(query);
    }

    @RequestMapping(value={"/admin/quartz/job/find", "/sys/quartz/job/find"})
    @ResponseBody
    public Result<List<QuartzJob>> findJob(@RequestBody Query query){
        return Result.forSuccess(quartzManagerService.findJob(query));
    }

    /**
     * 查询触发器
     * @param query
     * @return
     */
    @RequestMapping(value={"/admin/quartz/trigger/page", "/sys/quartz/trigger/page"})
    @ResponseBody
    public PageResult<List<QuartzTrigger>> pageTrigger(@RequestBody Query query){
        return quartzManagerService.pageTrigger(query);
    }

    @RequestMapping(value={"/admin/quartz/trigger/find", "/sys/quartz/trigger/find"})
    @ResponseBody
    public Result<List<QuartzTrigger>> findTrigger(@RequestBody Query query){
        return Result.forSuccess(quartzManagerService.findTrigger(query));
    }

    /**
     * 查询日历
     * @param query
     * @return
     */
    @RequestMapping(value={"/admin/quartz/calendar/page", "/sys/quartz/calendar/page"})
    @ResponseBody
    public PageResult<List<QuartzCalendar>> pageCalendar(@RequestBody Query query){
        return quartzManagerService.pageCalendar(query);
    }

    @RequestMapping(value={"/admin/quartz/calendar/find", "/sys/quartz/calendar/find"})
    @ResponseBody
    public Result<List<QuartzCalendar>> findCalendar(@RequestBody Query query){
        return Result.forSuccess(quartzManagerService.findCalendar(query));
    }

    @RequestMapping(value={"/admin/quartz/job/add", "/sys/quartz/job/add"})
    @ResponseBody
    public Result<Void> addJob(@RequestBody JobData jobData) {
        return scheduleManager.addJob(jobData, CommonJob.class) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 暂停任务
     *
     */
    @RequestMapping(value={"/admin/quartz/job/pause", "/sys/quartz/job/pause"})
    @ResponseBody
    public Result<Void> pauseJob(@RequestParam String group, @RequestParam String name) {
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }

        return scheduleManager.pauseJob(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 移除任务
     *
     */
    @RequestMapping(value={"/admin/quartz/job/remove", "/sys/quartz/job/remove"})
    @ResponseBody
    public Result<Void> removeJob(@RequestParam String group, @RequestParam String name){
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.removeJob(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 恢复任务
     *
     */
    @RequestMapping(value={"/admin/quartz/job/resume", "/sys/quartz/job/resume"})
    @ResponseBody
    public Result<Void> resumeJob(@RequestParam String group, @RequestParam String name){
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.resumeJob(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 直接为某个key和group组成的任务添加一个触发器
     * @param triggerData 触发器信息
     * @return
     */
    @RequestMapping(value={"/admin/quartz/trigger/add", "/sys/quartz/trigger/add"})
    @ResponseBody
    public Result<Void> addTrigger(@RequestBody TriggerData triggerData) {
        return scheduleManager.addTrigger(triggerData) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 暂停触发器
     * @return
     */
    @RequestMapping(value={"/admin/quartz/trigger/pause", "/sys/quartz/trigger/pause"})
    @ResponseBody
    public Result<Void> pauseTrigger(@RequestParam String group, @RequestParam String name) {
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.pauseTrigger(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    /**
     * 恢复触发器
     * @return
     */
    @RequestMapping(value={"/admin/quartz/trigger/resume", "/sys/quartz/trigger/resume"})
    @ResponseBody
    public Result<Void> resumeTrigger(@RequestParam String group, @RequestParam String name) {
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.resumeTrigger(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    @RequestMapping(value={"/admin/quartz/trigger/remove", "/sys/quartz/trigger/remove"})
    @ResponseBody
    public Result<Void> removeTrigger(@RequestParam String group, @RequestParam String name) {
        if(StringUtils.isEmpty(group)){
            return Result.forError("NO_GROUP", "");
        }
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.removeTrigger(new Key(group, name)) ? Result.forSuccess() : Result.forError();
    }

    @RequestMapping(value={"/admin/quartz/calendar/add", "/sys/quartz/calendar/add"})
    @ResponseBody
    public Result<Void> addCanlendar(@RequestBody CalendarData calendarData) {
        return scheduleManager.addCanlendar(calendarData) ? Result.forSuccess() : Result.forError();
    }

    @RequestMapping(value={"/admin/quartz/calendar/remove", "/sys/quartz/calendar/remove"})
    @ResponseBody
    public Result<Void> removeCalendar(@RequestParam String name) {
        if(StringUtils.isEmpty(name)){
            return Result.forError("NO_NAME", "");
        }
        return scheduleManager.removeCalendar(name) ? Result.forSuccess() : Result.forError();
    }
}
