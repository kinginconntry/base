package com.needto.quartz.entity;

import com.needto.common.context.SpringEnv;
import com.needto.quartz.data.Constants;
import com.needto.tool.entity.Dict;
import org.quartz.*;

/**
 * @author Administrator
 *
 * http 任务
 */
public class CommonJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        TriggerKey triggerKey = jobExecutionContext.getTrigger().getKey();
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        Dict data = new Dict();
        data.putAll(dataMap);
        TaskData taskData = new TaskData();
        taskData.setData(data);
        taskData.setJobGroup(jobKey.getGroup());
        taskData.setJobName(jobKey.getName());
        taskData.setTriggerGroup(triggerKey.getGroup());
        taskData.setTriggerName(triggerKey.getName());
        SpringEnv.getApplicationContext().publishEvent(new TaskEvent(this, taskData, data.getValue(Constants.DEAL_KEY)));
    }
}
