package com.needto.cipher.service;

import com.needto.cipher.model.ApiAccess;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.inter.CommonDao;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.Utils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Administrator
 * 目标api调用
 */
public class ApiAccessService<T extends ApiAccess> {


    @Resource
    private CommonDao commonDao;

    public T save(T apiAccess){
        Assert.validateNull(apiAccess);
        apiAccess.setAccessId(Utils.getGuid());
        apiAccess.setSecret(Utils.getGuidBase64());
        return this.commonDao.save(apiAccess, ApiAccess.TABLE);
    }

    public List<T> find(List<Filter> filters, List<Order> orders, Class<T> obj){
        return this.commonDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), obj, ApiAccess.TABLE);
    }
}
