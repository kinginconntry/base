package com.needto.user;

import com.needto.common.entity.Filter;
import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.utils.Assert;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.PageDataResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 日志服务
 */
@Service
public class OpLogService {

    private static final Logger LOG = LoggerFactory.getLogger(OpLogService.class);

    @Autowired
    private CommonDao commonDao;

    public <T extends OpLog> T save(T oplog){
        Assert.validateNull(oplog);
        Assert.validateNull(oplog.getOwner());
        Assert.validateNull(oplog.getTarget());
        return this.commonDao.save(oplog, OpLog.TABLE);
    }

    public PageResult<List<OpLog>> findByPage(Query query, String owner){
        Assert.validateStringEmpty(owner);
        if(query == null){
            query = new Query();
        }

        if(query.getFilters() == null){
            query.setFilters(new ArrayList<>(1));
        }
        query.getFilters().add(new Filter("owner", owner));
        PageDataResult<OpLog> res = this.commonDao.findByPage(CommonQueryUtils.getQuery(query), OpLog.class, OpLog.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    public PageResult<List<OpLog>> findByPage(Query query){
        if(query == null){
            query = new Query();
        }
        PageDataResult<OpLog> res = this.commonDao.findByPage(CommonQueryUtils.getQuery(query), OpLog.class, OpLog.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    public long deleteByFilter(List<Filter> filterList){
        return this.commonDao.delete(CommonQueryUtils.getFilters(filterList), OpLog.TABLE);
    }

    public long delete(List<String> ids){
        Assert.validateNull(ids);
        return this.commonDao.deleteByIds(ids, OpLog.TABLE);
    }
}
