package com.needto.services.user.resourceperm;

import com.needto.common.entity.FieldFilter;
import com.needto.common.entity.TreeData;
import com.needto.common.utils.Assert;
import com.needto.common.utils.TreeUtils;
import com.needto.dao.common.CommonDao;
import com.needto.common.entity.FieldOrder;
import com.needto.services.user.resourceperm.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Administrator
 * 资源权限service
 */
@Service
public class PermissionService {

    @Autowired
    private CommonDao mongoDao;

    public Permission save(Permission permission){
        Assert.validateNull(permission);
        Assert.validateStringEmpty(permission.getCode(), "", "没有权限码");
        Assert.validateStringEmpty(permission.getName(), "", "没有权限名称");
        Assert.validateCondition(!StringUtils.isEmpty(permission.getPid()) && this.mongoDao.findOne(FieldFilter.single("pid", permission.getPid()), Permission.class, Permission.TABLE) == null, "父级权限节点不存在");
        Assert.validateCondition(this.mongoDao.findOne(FieldFilter.single("perm", permission.getPerm()), Permission.class, Permission.TABLE) != null, "权限码已存在，请更换");
        return this.mongoDao.save(permission, Permission.TABLE);
    }

    public List<Permission> find(List<FieldFilter> fieldFilters, List<FieldOrder> orders){
        return this.mongoDao.find(fieldFilters, orders, Permission.class, Permission.TABLE);
    }

    public List<TreeData> getHorizontal(List<FieldFilter> fieldFilters, List<FieldOrder> orders){
        return TreeUtils.getHorizontal(this.find(fieldFilters, orders));
    }

    public long deleteIds(List<String> ids){
        Assert.validateNull(ids);
        return this.mongoDao.deleteByIds(ids, Permission.TABLE);
    }

}
