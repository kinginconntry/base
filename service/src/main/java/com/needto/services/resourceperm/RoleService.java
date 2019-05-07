package com.needto.services.resourceperm;

import com.google.common.collect.Lists;
import com.needto.common.dao.common.CommonDao;
import com.needto.common.dao.common.FieldFilter;
import com.needto.common.dao.common.FieldOrder;
import com.needto.common.dao.mongo.MongoQueryUtils;
import com.needto.common.entity.Target;
import com.needto.common.services.resourceperm.entity.Role;
import com.needto.common.services.resourceperm.entity.RoleRelation;
import com.needto.common.utils.Assert;
import org.bson.types.ObjectId;
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
    private CommonDao mongoDao;

    public Role save(Role role){
        Assert.validateNull(role);
        Assert.validateStringEmpty(role.getOwner());
        Assert.validateStringEmpty(role.getName());
        return this.mongoDao.save(role, Role.TABLE);
    }

    public Long deleteByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.mongoDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", MongoQueryUtils.MongoOp.IN.name(), ids)
        ), Role.TABLE);
    }

    public List<Role> find(String owner, List<FieldFilter> fieldFilters, List<FieldOrder> orders){
        Assert.validateStringEmpty(owner);
        if(fieldFilters == null){
            fieldFilters = new ArrayList<>(1);
        }
        fieldFilters.add(new FieldFilter("owner", owner));
        return this.mongoDao.find(fieldFilters, orders, Role.class, Role.TABLE);
    }

    public List<Role> findByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", MongoQueryUtils.MongoOp.IN.name(), ids)
        ), Role.class, Role.TABLE);
    }

    public RoleRelation saveRelation(RoleRelation roleRelation){
        Assert.validateNull(roleRelation);
        Assert.validateStringEmpty(roleRelation.getOwner());
        Assert.validateCondition(ObjectId.isValid(roleRelation.getRole()), "");
        Assert.validateNull(roleRelation.getBelongto());
        Assert.validateStringEmpty(roleRelation.getBelongto().getType());
        Assert.validateStringEmpty(roleRelation.getBelongto().getGuid());
        RoleRelation old = this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", roleRelation.getOwner()),
                new FieldFilter("role", roleRelation.getRole()),
                new FieldFilter("belongto.guid", roleRelation.getBelongto().getGuid()),
                new FieldFilter("belongto.type", roleRelation.getBelongto().getType())
        ), RoleRelation.class, RoleRelation.TABLE);
        if(old == null){
            roleRelation = this.mongoDao.save(roleRelation, RoleRelation.TABLE);;
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
        return this.mongoDao.delete(Lists.newArrayList(
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
        List<RoleRelation> roleRelations = this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("belongto.guid", belongto.getGuid()),
                new FieldFilter("belongto.type", belongto.getType())
        ), RoleRelation.class, RoleRelation.TABLE);
        List<Role> roles;
        if(!CollectionUtils.isEmpty(roleRelations)){
            Set<String> roleIds = new HashSet<>();
            roleRelations.forEach(v -> roleIds.add(v.getRole()));
            roles = this.findByIds(owner, new ArrayList<>(roleIds));
        }else{
            roles = new ArrayList<>(0);
        }
        return roles;
    }
}
