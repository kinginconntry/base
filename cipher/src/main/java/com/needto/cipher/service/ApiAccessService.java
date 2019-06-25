package com.needto.cipher.service;

import com.google.common.collect.Lists;
import com.needto.cipher.model.ApiAccess;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.inter.CommonDao;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.SignUtils;
import com.needto.tool.utils.Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean validate(String accessId, Map<String, Object> attrs, String sign){
        if(StringUtils.isEmpty(accessId) || StringUtils.isEmpty(sign)){
            return false;
        }
        ApiAccess apiAccess = commonDao.findOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId)
        ), ApiAccess.class, ApiAccess.TABLE);
        if(apiAccess == null){
            return false;
        }
        if(attrs == null){
            attrs = new HashMap<>();
        }
        attrs.put("accessId", apiAccess.getAccessId());
        String sg = SignUtils.getParamSign(attrs, apiAccess.getSecret(), apiAccess.getCrypto().key);
        return sign.equals(sg);
    }
}
