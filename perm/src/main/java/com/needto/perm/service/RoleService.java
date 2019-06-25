package com.needto.perm.service;

import com.google.common.collect.Lists;
import com.needto.common.entity.Query;
import com.needto.common.entity.Target;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
import com.needto.perm.model.Role;
import com.needto.perm.model.RoleRelation;
import com.needto.tool.entity.Filter;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 * 角色服务
 */
@Service
public class RoleService {

    @Autowired
    private CommonDao commonDao;

    public <T extends Role> T save(T role){
        Assert.validateNull(role);
        Assert.validateStringEmpty(role.getName());
        return this.commonDao.save(role, Role.TABLE);
    }

    public Long delete(List<Filter> filters){
        return this.commonDao.delete(CommonQueryUtils.getFilters(filters), Role.TABLE);
    }

    public <T extends Role> List<T> find(Query query, Class<T> obj){
        return this.commonDao.find(CommonQueryUtils.getQuery(query), obj, Role.TABLE);
    }

    public <T extends Role> List<T> findByIds(List<String> ids, Class<T> obj){
        Assert.validateNull(ids);
        return this.commonDao.findByIds(ids, obj, Role.TABLE);
    }

    public <T extends Role> List<T> findByDataPerms(List<String> dataPerms, Class<T> obj){
        if(CollectionUtils.isEmpty(dataPerms)){
            return new ArrayList<>(0);
        }
        return commonDao.find(Lists.newArrayList(
                new FieldFilter("dataperms", Op.IN.name(), dataPerms)
        ), obj, Role.TABLE);
    }

    public <T extends RoleRelation> List<T> findByRoleIds(List<String> roleIds, Class<T> obj){
        if(CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>(0);
        }
        return commonDao.find(Lists.newArrayList(
                new FieldFilter("roles", Op.IN.name(), roleIds)
        ), obj, RoleRelation.TABLE);
    }


    public RoleRelation saveRelation(RoleRelation roleRelation){
        Assert.validateNull(roleRelation);
        Assert.validateNull(roleRelation.getBelongto());
        Assert.validateStringEmpty(roleRelation.getBelongto().getType());
        Assert.validateStringEmpty(roleRelation.getBelongto().getGuid());
        return this.commonDao.save(roleRelation, RoleRelation.TABLE);
    }
}
