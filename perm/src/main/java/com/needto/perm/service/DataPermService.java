package com.needto.perm.service;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.common.entity.Update;
import com.needto.common.utils.Assert;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
import com.needto.perm.data.DataPermEntity;
import com.needto.perm.model.DataPerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * 数据权限service
 */
@Service
public class DataPermService {

    private static final Logger LOG = LoggerFactory.getLogger(DataPermService.class);

    @Autowired
    private CommonDao commonDao;

    public DataPerm save(DataPerm dataPerm){
        Assert.validateNull(dataPerm);
        Assert.validateStringEmpty(dataPerm.getOwner());
        Assert.validateNull(dataPerm.getDataPermData());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getSource());
        Assert.validateNull(dataPerm.getDataPermData().getTarget());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getTarget().getType());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getTarget().getGuid());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getOp());
        Assert.validateNull(dataPerm.getDataPermData().getOperable());
        return this.commonDao.save(dataPerm, DataPerm.TABLE);
    }

    public List<DataPerm> findByFilter(String owner){
        Assert.validateStringEmpty(owner);
        return this.commonDao.find(FieldFilter.single("owner", owner), DataPerm.class, DataPerm.TABLE);
    }

    public List<DataPerm> findByFilter(String owner, Target source){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        return this.commonDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("dataPermData.source.type", source.getType()),
                new FieldFilter("dataPermData.source.guid", source.getGuid())
        ), DataPerm.class, DataPerm.TABLE);
    }

    public List<DataPerm> findByFilter(String owner, Target source, Target target){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        Assert.validateNull(target);
        Assert.validateStringEmpty(target.getType());
        Assert.validateStringEmpty(target.getGuid());
        return this.commonDao.find(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("dataPermData.source.type", source.getType()),
                new FieldFilter("dataPermData.source.guid", source.getGuid()),
                new FieldFilter("dataPermData.target.type", target.getType()),
                new FieldFilter("dataPermData.target.guid", target.getGuid())
        ), DataPerm.class, DataPerm.TABLE);
    }

    public List<DataPermEntity> getDataPermEntitysByIds(List<String> ids){
        if(CollectionUtils.isEmpty(ids)){
            return new ArrayList<>(0);
        }
        List<DataPerm> dataPerms = this.commonDao.findByIds(ids, DataPerm.class, DataPerm.TABLE);
        List<DataPermEntity> list = new ArrayList<>(dataPerms.size());
        dataPerms.forEach(dataPerm -> list.add(dataPerm.getDataPermData()));
        return list;
    }

    public DataPerm findByFilter(String owner, Target source, Target target, String op, boolean operable){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(source);
        Assert.validateNull(target);
        Assert.validateStringEmpty(target.getType());
        Assert.validateStringEmpty(target.getGuid());
        return this.commonDao.findOne(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("dataPermData.source.type", source.getType()),
                new FieldFilter("dataPermData.source.guid", source.getGuid()),
                new FieldFilter("dataPermData.target.type", target.getType()),
                new FieldFilter("dataPermData.target.guid", target.getGuid()),
                new FieldFilter("dataPermData.op", op),
                new FieldFilter("dataPermData.operable", operable)
        ), DataPerm.class, DataPerm.TABLE);
    }

    public Long deleteByIds(String owner, List<String> ids){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.commonDao.delete(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), DataPerm.TABLE);
    }

    public Long update(String owner, List<String> ids, List<Update> updates){
        Assert.validateStringEmpty(owner);
        Assert.validateNull(ids);
        return this.commonDao.update(Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("id", Op.IN.name(), ids)
        ), CommonQueryUtils.getUpdates(updates), DataPerm.TABLE);
    }
}
