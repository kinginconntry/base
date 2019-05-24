package com.needto.quartz.entity;

import com.needto.common.exception.LogicException;
import org.quartz.CronExpression;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Administrator
 * 触发器类型
 */
public enum TriggerType {
    /**
     * cron表达式
     */
    CRON{
        @Override
        public Trigger getTrigger(TriggerData triggerData) {
            String cron = triggerData.getConfig().getValue("cron");
            Assert.validateStringEmpty(cron);
            CronTriggerImpl ctrigger = new CronTriggerImpl();
            ctrigger.setKey(new TriggerKey(triggerData.getName(), triggerData.getGroup()));
            try {
                ctrigger.setCronExpression(new CronExpression(cron));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new LogicException("ILLEGAL_CRON", "");
            }
            ctrigger.setDescription(triggerData.getDesc());
            ctrigger.setJobName(triggerData.getJobName());
            ctrigger.setJobGroup(triggerData.getJobGroup());
            ctrigger.setCalendarName(triggerData.getCalanderName());
            return ctrigger;
        }
    },
    /**
     * 简单触发器
     */
    SIMPLE{
        @Override
        public Trigger getTrigger(TriggerData triggerData) {
            Dict config = triggerData.getConfig();
            String expression = config.getValue("expression");
            Assert.validateStringEmpty(expression);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleTriggerImpl strigger = new SimpleTriggerImpl();
            strigger.setKey(new TriggerKey(triggerData.getName(), triggerData.getGroup()));
            strigger.setJobName(triggerData.getJobName());
            strigger.setJobGroup(triggerData.getJobGroup());

            try {
                strigger.setStartTime(sdf.parse(expression));
            } catch (ParseException e) {
                e.printStackTrace();
                throw new LogicException("ILLEGAL_FORMAT", "");
            }
            // 设置超时策略为立即执行
            strigger.setMisfireInstruction(Trigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
            //指定重复次数（将执行次数转化为重复次数）
            int scountCount = config.getValue("scount", 1);
            int repeat = 0;
            if(scountCount > 0){
                repeat = scountCount - 1;
            }

            if (repeat > 0) {
                Long repeatInterval = config.getValue("sinterval");
                Assert.validateNull(repeatInterval, "INVALID_SINTERVAL", "");
                Assert.validateCondition(repeatInterval <= 0, "SINTERVAL_LESS_THAN_ZERO", "");
                strigger.setRepeatInterval(repeatInterval);
                strigger.setRepeatCount(repeat);
            }
            strigger.setDescription(triggerData.getDesc());
            strigger.setCalendarName(triggerData.getCalanderName());
            return strigger;
        }
    };

    public abstract Trigger getTrigger(TriggerData triggerData);
}
