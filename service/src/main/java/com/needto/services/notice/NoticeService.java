package com.needto.services.notice;

import com.google.common.collect.Lists;
import com.needto.common.exception.ValidateException;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.FieldFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;


/**
 * @author Administrator
 * 站内消息服务
 */
@Service
public class NoticeService {

    private static final Logger LOG = LoggerFactory.getLogger(NoticeService.class);

    @Autowired
    private CommonDao mongoDao;

    @Autowired
    private MongoOperations mongoOperations;

    /**
     * 保存站内消息
     * @param notice
     * @return
     */
    public Notice saveNotice(Notice notice){
        if(StringUtils.isEmpty(notice.getType())){
            throw new ValidateException("NO_TYPE", "");
        }

        if(notice.getSource() == null){
            throw new ValidateException("NO_SOURCE", "");
        }

        if(notice.getData() == null){
            throw new ValidateException("NO_DATA", "");
        }

        if(notice.getTarget() == null){
            throw new ValidateException("NO_TARGET", "");
        }

        Date now = new Date();
        if(notice.getStartTime() == null){
            notice.setStartTime(now);
        }

        if(notice.getEndTime() == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(3000, 1, 1);
            notice.setEndTime(calendar.getTime());
        }

        if(notice.getStartTime().getTime() >= notice.getEndTime().getTime()){
            throw new ValidateException("INVALID", "");
        }

        if(notice.getVersion() == 0){
            notice.initVersion();
        }
        if(mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("type", notice.getType()),
                new FieldFilter("version", notice.getVersion())
        ), Notice.class, Notice.TABLE) != null){
            throw new ValidateException("NO_CREATE", "");
        }
        return mongoDao.save(notice, Notice.TABLE);
    }

    public long deleteByIds(List<String> ids){
        return mongoDao.deleteByIds(ids, Notice.TABLE);
    }

    /**
     * 获取消息
     * @param type
     * @param targetType
     * @param target
     * @return
     */
    public List<Notice> getNotices(String type, String targetType, String target){
        if(StringUtils.isEmpty(targetType) || StringUtils.isEmpty(target)){
            return new ArrayList<>(0);
        }
        List<FieldFilter> targetFilters = Lists.newArrayList(
                new FieldFilter("target.type", targetType),
                new FieldFilter("target.guid", target)
        );
        if(!StringUtils.isEmpty(type)){
            targetFilters.add(new FieldFilter("type", type));
        }
        List<TargetVersion> targetVersions = mongoDao.find(targetFilters, TargetVersion.class, TargetVersion.TABLE);
        Date now = new Date();
        Criteria criteria = Criteria.where("startTime").lte(now).and("endTime").gte(now);
        List<Criteria> orList = new ArrayList<>();
        Map<String, TargetVersion> map = new HashMap<>(Notice.Type.values().length);
        if(!CollectionUtils.isEmpty(targetVersions)){
            for (TargetVersion targetVersion : targetVersions){
                map.put(targetVersion.getType(), targetVersion);
            }
        }
        for(Notice.Type temp : Notice.Type.values()){
            if(temp.name().equals(Notice.Type.BROADCAST.name())){
                if(map.containsKey(Notice.Type.BROADCAST.name())){
                    orList.add(Criteria.where("type").is(temp.name()).and("version").gt(map.get(temp.name()).getVersion()));
                }else{
                    orList.add(Criteria.where("type").is(temp.name()));
                }
            }else{
                if(map.containsKey(temp.name())){
                    orList.add(Criteria.where("type").is(temp.name()).and("target.type").is(targetType).and("target.guid").is(target).and("version").gt(map.get(temp.name()).getVersion()));
                }else{
                    orList.add(Criteria.where("type").is(temp.name()).and("target.type").is(targetType).and("target.guid").is(target));
                }
            }

        }
        Criteria[] criteriaArray = new Criteria[orList.size()];
        criteria.orOperator(orList.toArray(criteriaArray));
        return mongoOperations.find(new Query(criteria), Notice.class, Notice.TABLE);
    }

    /**
     * 确认消息版本
     * @param targetVersion
     * @return
     */
    public boolean ackNotice(TargetVersion targetVersion){
        if(targetVersion == null){
            throw new ValidateException("NO_DATA", "");
        }
        if(StringUtils.isEmpty(targetVersion.getType())){
            throw new ValidateException("NO_DATA", "");
        }
        if(targetVersion.getTarget() == null){
            throw new ValidateException("NO_TARGET", "");
        }
        TargetVersion old = mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("type", targetVersion.getType()),
                new FieldFilter("target.type", targetVersion.getTarget().getType()),
                new FieldFilter("target.guid", targetVersion.getTarget().getGuid())
        ), TargetVersion.class, TargetVersion.TABLE);
        if(old != null){
            if(targetVersion.getVersion() > old.getVersion()){
                old.setVersion(targetVersion.getVersion());
                if(mongoDao.save(old, TargetVersion.TABLE) != null){
                    return true;
                }else{
                    return false;
                }
            }else{
                throw new ValidateException("NO_UPDATE", "");
            }
        }else{
            if(mongoDao.save(targetVersion, TargetVersion.TABLE) == null){
                return false;
            }else{
                return true;
            }
        }

    }

    /**
     * 创建系统公告
     * @param msg
     * @return
     */
    public Notice createBroadCast(BroadCastMsg msg, Date start, Date end){
        if(msg == null){
            throw new ValidateException("NO_DATA", "");
        }
        if(StringUtils.isEmpty(msg.getContent())){
            throw new ValidateException("NO_CONTENT", "");
        }
        Notice notice = new Notice();
        notice.setType(Notice.Type.BROADCAST.name());
        notice.getSource().setType(Notice.SYS_SOURCE_TYPE);
        notice.getSource().setGuid(Notice.SYS_SOURCE);
        notice.setData(msg);
        notice.setStartTime(start);
        notice.setEndTime(end);
        notice.setType(Notice.Type.BROADCAST.name());
        return this.saveNotice(notice);
    }


}
