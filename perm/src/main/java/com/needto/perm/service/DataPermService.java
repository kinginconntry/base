package com.needto.perm.service;

import com.google.common.collect.Lists;
import com.needto.common.entity.Target;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
import com.needto.perm.data.DataPermEntity;
import com.needto.perm.model.DataPerm;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Update;
import com.needto.tool.utils.Assert;
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

    public <T extends DataPerm> T save(T dataPerm){
        Assert.validateNull(dataPerm);
        Assert.validateNull(dataPerm.getDataPermData());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getSource());
        Assert.validateNull(dataPerm.getDataPermData().getTarget());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getTarget().getType());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getTarget().getGuid());
        Assert.validateStringEmpty(dataPerm.getDataPermData().getOp());
        Assert.validateNull(dataPerm.getDataPermData().getOperable());
        return this.commonDao.save(dataPerm, DataPerm.TABLE);
    }

    public <T extends DataPerm> List<T> findByFilter(List<Filter> filters, Class<T> obj){
        return this.commonDao.find(CommonQueryUtils.getFilters(filters), obj, DataPerm.TABLE);
    }

    public Long delete(List<Filter> filters){
        return this.commonDao.delete(CommonQueryUtils.getFilters(filters), DataPerm.TABLE);
    }

    public Long update(List<Filter> filters, List<Update> updates){
        return this.commonDao.update(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getUpdates(updates), DataPerm.TABLE);
    }
}
