package com.needto.quartz.deal;

import com.needto.common.utils.Assert;
import com.needto.quartz.entity.TaskData;
import com.needto.quartz.entity.TaskEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Component
public class TaskDealContainer {

    private static final Log log = LogFactory.getLog(TaskDealContainer.class);

    private static final Map<String, TaskDeal> MAP = new HashMap<>();

    private static final Map<String, String> DESC_MAP = new HashMap<>();

    private static final Map<String, String> NAME_MAP = new HashMap<>();

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init(){
        Map<String, Object> filterMap = applicationContext.getBeansWithAnnotation(TaskDealRegister.class);
        filterMap.forEach((k, v) -> {
            if(v instanceof TaskDeal){
                TaskDeal callbackInstance =((TaskDeal) v);
                String code = callbackInstance.getClass().getAnnotation(TaskDealRegister.class).code();
                String name = callbackInstance.getClass().getAnnotation(TaskDealRegister.class).name();
                String desc = callbackInstance.getClass().getAnnotation(TaskDealRegister.class).desc();
                set(code, callbackInstance, name, desc);
            }
        });
    }

    public boolean contain(String code){
        if(StringUtils.isEmpty(code)){
            return false;
        }
        return MAP.containsKey(code);
    }

    public void set(String code, TaskDeal orderDeal, String name, String desc){
        if(StringUtils.isEmpty(code)){
            return;
        }
        Assert.validateCondition(MAP.containsKey(code), "code repeat");
        MAP.put(code, orderDeal);
        NAME_MAP.put(code, name);
        DESC_MAP.put(code, desc);
    }

    public TaskDeal get(String code){
        if(StringUtils.isEmpty(code)){
            return null;
        }
        return MAP.get(code);
    }


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
}
