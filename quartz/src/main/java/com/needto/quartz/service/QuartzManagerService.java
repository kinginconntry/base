package com.needto.quartz.service;

import com.needto.common.entity.Query;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.PageDataResult;
import com.needto.quartz.entity.TriggerType;
import com.needto.quartz.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
@Service
public class QuartzManagerService {

    @Autowired
    private CommonDao commonDao;

    /**
     * 查询调度器
     * @param query
     * @return
     */
    public List<QuartzScheduler> findScheduler(Query query){
        List<Dict> data = commonDao.find(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzScheduler> list = null;
        if(data != null){
            list = new ArrayList<>(data.size());
            for(Dict dict : data){
                list.add(QuartzScheduler.builder(dict));
            }
        }
        return list;
    }

    /**
     * 查询任务
     * @param query
     * @return
     */
    public PageResult<List<QuartzJob>> pageJob(Query query){
        PageDataResult<Dict> data = commonDao.findByPage(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzJob> list = null;
        if(data.getData() != null){
            list = new ArrayList<>(data.getData().size());
            for(Dict dict : data.getData()){
                list.add(QuartzJob.builder(dict));
            }
        }
        return PageResult.forSuccess(data.getTotal(), data.getPage(), list);
    }

    public List<QuartzJob> findJob(Query query){
        List<Dict> data = commonDao.find(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzJob> list = null;
        if(data != null){
            list = new ArrayList<>(data.size());
            for(Dict dict : data){
                list.add(QuartzJob.builder(dict));
            }
        }
        return list;
    }

    /**
     * 查询触发器
     * @param query
     * @return
     */
    public PageResult<List<QuartzTrigger>> pageTrigger(Query query){
        PageDataResult<Dict> data = commonDao.findByPage(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzTrigger> list = null;
        if(data.getData() != null){
            list = new ArrayList<>(data.getData().size());
            for(Dict dict : data.getData()){
                if(TriggerType.CRON.name().equals(dict.get("TRIGGER_TYPE"))){
                    list.add(QuartzCronTrigger.builder(dict));
                }else if(TriggerType.SIMPLE.name().equals(dict.get("TRIGGER_TYPE"))){
                    list.add(QuartzSimpleTrigger.builder(dict));
                }else{

                }
            }
        }
        return PageResult.forSuccess(data.getTotal(), data.getPage(), list);
    }

    public List<QuartzTrigger> findTrigger(Query query){
        List<Dict> data = commonDao.find(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzTrigger> list = null;
        if(data != null){
            list = new ArrayList<>(data.size());
            for(Dict dict : data){
                if(TriggerType.CRON.name().equals(dict.get("TRIGGER_TYPE"))){
                    list.add(QuartzCronTrigger.builder(dict));
                }else if(TriggerType.SIMPLE.name().equals(dict.get("TRIGGER_TYPE"))){
                    list.add(QuartzSimpleTrigger.builder(dict));
                }else{

                }
            }
        }
        return list;
    }


    /**
     * 查询日历
     * @param query
     * @return
     */
    public PageResult<List<QuartzCalendar>> pageCalendar(Query query){
        PageDataResult<Dict> data = commonDao.findByPage(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzCalendar> list = null;
        if(data.getData() != null){
            list = new ArrayList<>(data.getData().size());
            for(Dict dict : data.getData()){
                list.add(QuartzCalendar.builder(dict));
            }
        }
        return PageResult.forSuccess(data.getTotal(), data.getPage(), list);
    }

    public List<QuartzCalendar> findCalendar(Query query){
        List<Dict> data = commonDao.find(CommonQueryUtils.getQuery(query),  Dict.class, "");
        List<QuartzCalendar> list = null;
        if(data != null){
            list = new ArrayList<>(data.size());
            for(Dict dict : data){
                list.add(QuartzCalendar.builder(dict));
            }
        }
        return list;
    }
}
