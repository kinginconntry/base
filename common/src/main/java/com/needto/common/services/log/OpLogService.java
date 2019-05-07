package com.needto.common.services.log;

import com.needto.common.dao.common.CommonDao;
import com.needto.common.dao.common.CommonQuery;
import com.needto.common.dao.common.FieldFilter;
import com.needto.common.dao.common.PageDataResult;
import com.needto.common.entity.PageResult;
import com.needto.common.utils.Assert;
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

    public PageResult<List<OpLog>> findByPage(CommonQuery commonQuery, String owner){
        Assert.validateStringEmpty(owner);
        if(commonQuery.getFilters() == null){
            commonQuery.setFilters(new ArrayList<>(1));
        }
        commonQuery.getFilters().add(new FieldFilter("owner", owner));
        PageDataResult<OpLog> res = this.commonDao.findByPage(commonQuery, OpLog.class, OpLog.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    public PageResult<List<OpLog>> findByPage(CommonQuery commonQuery){
        if(commonQuery.getFilters() == null){
            commonQuery.setFilters(new ArrayList<>(1));
        }
        PageDataResult<OpLog> res = this.commonDao.findByPage(commonQuery, OpLog.class, OpLog.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    public long deleteByFilter(List<FieldFilter> filterList){
        return this.commonDao.delete(filterList, OpLog.TABLE);
    }

    public long delete(List<String> ids){
        Assert.validateNull(ids);
        return this.commonDao.deleteByIds(ids, OpLog.TABLE);
    }
}
