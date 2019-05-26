package com.needto.group;

import com.google.common.collect.Lists;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
import com.needto.tool.entity.TreeData;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Administrator
 * 分组服务
 */
@Service
public class GroupService {

    @Autowired
    private CommonDao mongoDao;

    public String getTable(String table){
        Assert.validateStringEmpty(table, "table can not be empty");
        return "_group_" + table;
    }

    /**
     * 根据主账户信息与所属获取所有分组
     * @param owner
     * @param table
     * @return
     */
    public List<Group> findByOwner(String owner, String table){
        Assert.validateStringEmpty(owner, "");
        return this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner)
        ), Group.class, getTable(table));
    }

    /**
     * 获取分组水平结构
     * @param owner
     * @param table
     * @return
     */
    public List<TreeData> getHorizontal(String owner, String table){
        return TreeUtils.getHorizontal(findByOwner(owner, table));
    }

    /**
     * 保存分组
     * @param group
     * @param table
     * @return
     */
    public Group save(Group group, String table){
        Assert.validateNull(group, "NO_GROUP");
        Assert.validateStringEmpty(group.getOwner(), "NO_OWNER");
        Assert.validateStringEmpty(group.getName(), "NO_NAME", "");
        String combineTable = getTable(table);
        Assert.validateCondition(!StringUtils.isEmpty(group.getPcode()) && this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", group.getOwner())
        ), Group.class, combineTable) == null, "PCODE_NOT_EXISTS", "");
        return this.mongoDao.save(group, combineTable);
    }

    /**
     * 删除分组
     * @param owner
     * @param table
     * @param ids
     * @return
     */
    public Long deleteByIds(String owner, List<String> ids, String table){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.mongoDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), getTable(table));
    }

}
