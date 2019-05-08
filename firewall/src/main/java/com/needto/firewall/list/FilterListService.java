package com.needto.firewall.list;

import com.google.common.collect.Lists;
import com.needto.common.entity.PageResult;
import com.needto.common.entity.Target;
import com.needto.common.utils.Assert;
import com.needto.common.utils.DateUtils;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.CommonQuery;
import com.needto.dao.common.PageDataResult;
import com.needto.dao.common.FieldFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 黑白名单服务，拦截或放行一切可能的名单
 */
@Service
public class FilterListService {

    private static final Logger LOG = LoggerFactory.getLogger(FilterListService.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CommonDao mongoDao;

    /**
     * 保存黑名单
     * @param filterList
     * @return
     */
    public FilterList save(FilterList filterList){
        Assert.validateNull(filterList);
        Assert.validateStringEmpty(filterList.getMode(), "mode can not be empty");
        Assert.validateNull(filterList.getTarget());
        Assert.validateStringEmpty(filterList.getTarget().getType(), "type can not be empty");
        Assert.validateStringEmpty(filterList.getTarget().getGuid(), "guid can not be empty");
        Assert.validateNull(filterList.getStart(), "start time can not be null");

        if(filterList.getEnd() != null){
            Assert.validateCondition(filterList.getStart().getTime() >= filterList.getEnd().getTime(), "start can not be more than end");
            Assert.validateCondition(filterList.getEnd().getTime() <= System.currentTimeMillis(), "end can not be less than currentTime");
        }

        FilterList temp = this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("mode", filterList.getMode()),
                new FieldFilter("target.type", filterList.getTarget().getType()),
                new FieldFilter("target.guid", filterList.getTarget().getGuid())
        ), FilterList.class, FilterList.TABLE);
        if(temp != null){
            temp.setEnd(filterList.getEnd());
            temp.setStart(filterList.getStart());
        }
        temp = this.mongoDao.save(filterList, FilterList.TABLE);
        if(temp != null){
            applicationContext.publishEvent(new SetListEvent(this, temp));
        }
        return temp;
    }

    /**
     * 删除黑名单
     * @param filterList
     * @return
     */
    public long deleteByFilter(List<FieldFilter> filterList){
        List<FilterList> filterLists = this.mongoDao.find(filterList, FilterList.class, FilterList.TABLE);
        if(!CollectionUtils.isEmpty(filterLists)){
            applicationContext.publishEvent(new RemoveListEvent(this, filterLists));
            return this.mongoDao.delete(filterList, FilterList.TABLE);
        }
        return 0L;
    }

    /**
     * 分页查找黑名单
     * @param commonQuery
     * @return
     */
    public PageResult<List<FilterList>> findByPage(CommonQuery commonQuery){
        PageDataResult<FilterList> res = this.mongoDao.findByPage(commonQuery, FilterList.class, FilterList.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    /**
     * 查找今日有效的黑名单
     * @param mode
     * @param target
     * @return
     */
    public FilterList findValid(String mode, Target target){
        Criteria or0 = Criteria.where("end").exists(false);
        Criteria or1 = Criteria.where("end").gte(new Date());
        Criteria criteria = Criteria.where("mode").is(mode).and("target.type").is(target.getType()).and("target.guid").is(target.getGuid()).and("start").gte(DateUtils.getBeginDate(0)).lt(DateUtils.getBeginDate(1)).orOperator(or0, or1);
        return this.mongoDao.findOne(criteria.getCriteriaObject().toJson(), FilterList.class, FilterList.TABLE);
    }
}
