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

    public Role save(Role role){
        Assert.validateNull(role);
        Assert.validateStringEmpty(role.getOwner());
        Assert.validateStringEmpty(role.getName());
        return this.commonDao.save(role, Role.TABLE);
    }

    public Long deleteByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.commonDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), Role.TABLE);
    }

    public List<Role> find(String owner, Query query){
        Assert.validateStringEmpty(owner);
        if(query == null){
            query = new Query();
        }
        List<Filter> filters = query.getFilters();
        filters.add(new Filter("owner", owner));
        query.setFilters(filters);
        return this.commonDao.find(CommonQueryUtils.getQuery(query), Role.class, Role.TABLE);
    }

    public List<Role> findByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.commonDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), Role.class, Role.TABLE);
    }

    public List<Role> findByIds(List<String> ids){
        Assert.validateNull(ids);
        return this.commonDao.findByIds(ids, Role.class, Role.TABLE);
    }

    public List<Role> findByOwner(String owner){
        Assert.validateStringEmpty(owner);
        return this.commonDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner)
        ), Role.class, Role.TABLE);
    }

    public List<Role> findByDataPerms(List<String> dataPerms){
        if(CollectionUtils.isEmpty(dataPerms)){
            return new ArrayList<>(0);
        }
        return commonDao.find(Lists.newArrayList(
                new FieldFilter("dataperms", Op.IN.name(), dataPerms)
        ), Role.class, Role.TABLE);
    }

    public List<RoleRelation> findByRoleIds(List<String> roleIds){
        if(CollectionUtils.isEmpty(roleIds)){
            return new ArrayList<>(0);
        }
        return commonDao.find(Lists.newArrayList(
                new FieldFilter("roles", Op.IN.name(), roleIds)
        ), RoleRelation.class, RoleRelation.TABLE);
    }


    public RoleRelation saveRelation(RoleRelation roleRelation){
        Assert.validateNull(roleRelation);
        Assert.validateStringEmpty(roleRelation.getOwner());
        Assert.validateNull(roleRelation.getBelongto());
        Assert.validateStringEmpty(roleRelation.getBelongto().getType());
        Assert.validateStringEmpty(roleRelation.getBelongto().getGuid());
        RoleRelation old = this.commonDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", roleRelation.getOwner()),
                new FieldFilter("belongto.guid", roleRelation.getBelongto().getGuid()),
                new FieldFilter("belongto.type", roleRelation.getBelongto().getType())
        ), RoleRelation.class, RoleRelation.TABLE);
        if(old == null){
            roleRelation = this.commonDao.save(roleRelation, RoleRelation.TABLE);;
        }else{
            roleRelation = old;
        }
        return roleRelation;
    }

    public Long deleteByOwnerAndBelongTo(String owner, Target belongto, String role){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(belongto);
        Assert.validateStringEmpty(belongto.getType());
        Assert.validateStringEmpty(belongto.getGuid());
        Assert.validateStringEmpty(role);
        return this.commonDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("role", role),
                new FieldFilter("belongto.guid", belongto.getGuid()),
                new FieldFilter("belongto.type", belongto.getType())
        ), RoleRelation.TABLE);
    }

    public List<Role> findByOwnerAndBelongTo(String owner, Target belongto){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(belongto);
        Assert.validateStringEmpty(belongto.getType());
        Assert.validateStringEmpty(belongto.getGuid());
        List<RoleRelation> roleRelations = this.commonDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("belongto.guid", belongto.getGuid()),
                new FieldFilter("belongto.type", belongto.getType())
        ), RoleRelation.class, RoleRelation.TABLE);
        List<Role> roles;
        if(!CollectionUtils.isEmpty(roleRelations)){
            Set<String> roleIds = new HashSet<>();
            roleRelations.forEach(v -> {
                if(v.getRoles() != null){
                    roleIds.addAll(v.getRoles());
                }
            });
            roles = this.findByIds(owner, new ArrayList<>(roleIds));
        }else{
            roles = new ArrayList<>(0);
        }
        return roles;
    }


}
