package com.needto.notice.service;

import com.google.common.collect.Lists;
import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Target;
import com.needto.common.exception.ValidateException;
import com.needto.common.utils.Assert;
import com.needto.common.utils.DateUtils;
import com.needto.dao.common.*;
import com.needto.dao.inter.CommonDao;
import com.needto.notice.data.Constant;
import com.needto.notice.event.NoticeChangeEvent;
import com.needto.notice.model.Notice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;


/**
 * @author Administrator
 * 系统消息服务
 */
@Service
public class NoticeService {

    private static final Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CommonDao commonDao;

    /**
     * 保存系统消息
     * @param notice
     * @return
     */
    public Notice save(Notice notice){

        if(notice.getData() == null){
            throw new ValidateException("NO_DATA", "");
        }

        if(notice.getTarget() == null){
            throw new ValidateException("NO_TARGET", "");
        }

        if(notice.getStartTime() == null){
            notice.setStartTime(new Date());
        }
        if(notice.getEndTime() == null){
            notice.setStartTime(DateUtils.FOREVER_DATE);
        }
        applicationContext.publishEvent(new NoticeChangeEvent(this, notice.getTarget()));
        return commonDao.save(notice, Notice.TABLE);
    }

    /**
     * 根据id删除公告
     * @param ids
     * @return
     */
    public long deleteByIds(List<String> ids){
        Assert.validateNull(ids);
        List<Notice> notices = this.commonDao.findByIds(ids, Notice.class, Notice.TABLE);
        List<Target> targets = new ArrayList<>();
        Set<String> targetIds = new HashSet<>();
        notices.forEach(v -> {
            if(!targetIds.contains(v.getTarget().getTargetId())){
                targets.add(v.getTarget());
                targetIds.add(v.getTarget().getTargetId());
            }
        });
        applicationContext.publishEvent(new NoticeChangeEvent(this, targets));
        return commonDao.deleteByIds(ids, Notice.TABLE);
    }

    /**
     * 创建系统公告
     * @param notice
     * @return
     */
    public Notice createBroadCast(Notice notice){
        if(notice == null){
            throw new ValidateException("NO_DATA", "");
        }
        return this.save(notice);
    }

    /**
     * 获取当前目标所有公告
     * @param target
     * @return
     */
    public List<Notice> getNotice(Target target){
        if(target == null){
            target = Constant.ALL_USER;
        }

        Date now = new Date();
        return this.commonDao.find(Lists.newArrayList(
            new FieldFilter("startTime", Op.LTE.name(), now),
            new FieldFilter("endTime", Op.GTE.name(), now),
            new FieldFilter("target.guid", target.getGuid()),
            new FieldFilter("target.type", target.getType())
        ), Notice.class, Notice.TABLE);
    }

    /**
     * 确认消息已读
     * @param target
     * @param ids
     * @return
     */
    public long ackNotice(Target target, List<String> ids){
        Assert.validateNull(target);
        Assert.validateCondition(CollectionUtils.isEmpty(ids), "NO_ID", "");
        return this.commonDao.update(Lists.newArrayList(
                new FieldFilter("id", Op.IN.name(), ids),
                new FieldFilter("target.guid", target.getGuid()),
                new FieldFilter("target.type", target.getType())
        ), Lists.newArrayList(
                new FieldUpdate("ack", true)
        ), Notice.TABLE);
    }

    public PageResult<List<Notice>> page(Query query){
        PageDataResult<Notice> res = this.commonDao.findByPage(CommonQueryUtils.getQuery(query), Notice.class, Notice.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }


}
