package com.needto.services.user.resourceperm;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.common.entity.Update;
import com.needto.common.utils.Assert;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.Op;
import com.needto.dao.models.FieldFilter;
import com.needto.services.user.resourceperm.entity.DataPerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * 数据权限service
 */
@Service
public class DataPermService {

    private static final Logger LOG = LoggerFactory.getLogger(DataPermService.class);

    @Autowired
    private CommonDao mongoDao;

    public DataPerm save(DataPerm dataPerm){
        Assert.validateNull(dataPerm);
        Assert.validateStringEmpty(dataPerm.getOwner());
        Assert.validateStringEmpty(dataPerm.getSource());
        Assert.validateNull(dataPerm.getTarget());
        Assert.validateStringEmpty(dataPerm.getTarget().getType());
        Assert.validateStringEmpty(dataPerm.getTarget().getGuid());
        Assert.validateCondition(!DataPerm.Op.contain(dataPerm.getOp()), "数据权限操作不支持");
        Assert.validateNull(dataPerm.getOperable());
        DataPerm old = this.findByFilter(dataPerm.getOwner(), dataPerm.getSource(), dataPerm.getTarget(), dataPerm.getOp(), dataPerm.getOperable());
        if(old == null){
            dataPerm = this.mongoDao.save(dataPerm, DataPerm.TABLE);
        }else{
            old.setCondition(dataPerm.getCondition());
            this.mongoDao.save(old, DataPerm.TABLE);
            dataPerm = old;
        }
        return dataPerm;
    }

    public List<DataPerm> findByFilter(String owner){
        Assert.validateStringEmpty(owner);
        return this.mongoDao.find(FieldFilter.single("owner", owner), DataPerm.class, DataPerm.TABLE);
    }

    public List<DataPerm> findByFilter(String owner, String source){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        return this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("source", source)
        ), DataPerm.class, DataPerm.TABLE);
    }

    public List<DataPerm> findByFilter(String owner, String source, Target target){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        Assert.validateNull(target);
        Assert.validateStringEmpty(target.getType());
        Assert.validateStringEmpty(target.getGuid());
        return this.mongoDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("source", source),
                new FieldFilter("target.type", target.getType()),
                new FieldFilter("target.guid", target.getGuid())
        ), DataPerm.class, DataPerm.TABLE);
    }

    public DataPerm findByFilter(String owner, String source, Target target, String op, boolean operable){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        Assert.validateNull(target);
        Assert.validateStringEmpty(target.getType());
        Assert.validateStringEmpty(target.getGuid());
        return this.mongoDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("source", source),
                new FieldFilter("target.type", target.getType()),
                new FieldFilter("target.guid", target.getGuid()),
                new FieldFilter("op", op),
                new FieldFilter("operable", operable)
        ), DataPerm.class, DataPerm.TABLE);
    }

    public Long deleteByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.mongoDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), DataPerm.TABLE);
    }

    public Long update(String owner, List<String> ids, List<Update> updates){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.mongoDao.update(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), CommonQueryUtils.getUpdates(updates), DataPerm.TABLE);
    }
}
