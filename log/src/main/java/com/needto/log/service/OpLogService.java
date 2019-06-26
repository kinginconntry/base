package com.needto.log.service;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.PageDataResult;
import com.needto.log.entity.OpLog;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * 日志服务
 */
@Service
public class OpLogService {


    @Autowired
    private CommonDao commonDao;

    public <T extends OpLog> T save(T oplog, String table){
        Assert.validateNull(oplog);
        Assert.validateNull(oplog.getTarget());
        return this.commonDao.save(oplog, OpLog.TABLE);
    }

    public <T extends OpLog> PageResult<List<T>> findByPage(Query query, Class<T> obj){
        return findByPage(query, obj, OpLog.TABLE);
    }

    public <T extends OpLog> PageResult<List<T>> findByPage(Query query, Class<T> obj, String table){
        if(query == null){
            query = new Query();
        }
        PageDataResult<T> res = this.commonDao.findByPage(CommonQueryUtils.getQuery(query), obj, table);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    public long delete(List<String> ids){
        return delete(ids, OpLog.TABLE);
    }

    public long delete(List<String> ids, String table){
        Assert.validateNull(ids);
        return this.commonDao.deleteByIds(ids, table);
    }

    public <T extends OpLog> List<T> find(List<Filter> filters, List<Order> orders, Class<T> obj, String table){
        return this.commonDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), obj, table);
    }

    public <T extends OpLog> List<T> find(List<Filter> filters, List<Order> orders, Class<T> obj){
        return find(filters, orders, obj, OpLog.TABLE);
    }
}
