package com.needto.group;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.common.entity.TreeData;
import com.needto.common.utils.Assert;
import com.needto.common.utils.TreeUtils;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
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

    /**
     * 根据主账户信息与所属获取所有分组
     * @param owner
     * @param belongTo
     * @return
     */
    public List<Group> findByOwnerAndBelongTo(String owner, Target belongTo){
        Assert.validateCondition(StringUtils.isEmpty(owner) || belongTo == null, "");
        Assert.validateCondition(StringUtils.isEmpty(belongTo.getType()) || StringUtils.isEmpty(belongTo.getGuid()), "");
        return this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("belongto.type", belongTo.getType()),
                new FieldFilter("belongto.guid", belongTo.getGuid())
        ), Group.class, Group.TABLE);
    }

    /**
     * 获取分组水平结构
     * @param owner
     * @param belongTo
     * @return
     */
    public List<TreeData> getHorizontal(String owner, Target belongTo){
        return TreeUtils.getHorizontal(findByOwnerAndBelongTo(owner, belongTo));
    }

    public Group save(Group group){
        Assert.validateNull(group, "NO_GROUP");
        Assert.validateStringEmpty(group.getOwner(), "NO_OWNER");
        Assert.validateNull(group.getBelongto(), "NO_BELONGTO");
        Assert.validateStringEmpty(group.getName(), "NO_NAME", "");
        Assert.validateStringEmpty(group.getBelongto().getType(), "NO_BELONGTO_TYPE");
        Assert.validateStringEmpty(group.getBelongto().getGuid(), "NO_BELONGTO_GUID");
        Assert.validateCondition(!StringUtils.isEmpty(group.getPcode()) && this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", group.getOwner()),
                new FieldFilter("belongto.type", group.getBelongto().getType()),
                new FieldFilter("belongto.guid", group.getBelongto().getGuid())
        ), Group.class, Group.TABLE) == null, "PCODE_NOT_EXISTS", "");
        return this.mongoDao.save(group, Group.TABLE);
    }

    public Long deleteByIds(String owner, Target belongTo, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(belongTo);
        Assert.validateStringEmpty(belongTo.getGuid());
        Assert.validateStringEmpty(belongTo.getType());
        Assert.validateNull(ids);
        return this.mongoDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("belongto.type", belongTo.getType()),
                new FieldFilter("belongto.guid", belongTo.getGuid()),
                new FieldFilter("id", Op.IN.name(), ids)
        ), Group.TABLE);
    }

}
