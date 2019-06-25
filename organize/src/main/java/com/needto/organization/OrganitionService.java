package com.needto.organization;

import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.inter.CommonDao;
import com.needto.tool.entity.Filter;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Administrator
 */
@Service
public class OrganitionService {

    @Autowired
    private CommonDao mongoDao;

    public <T extends Organition> T save(T organition){
        Assert.validateNull(organition);
        Assert.validateStringEmpty(organition.getId());
        Assert.validateNull(this.mongoDao.findById(organition.getId(), Organition.class, Organition.TABLE));
        return this.mongoDao.save(organition, Organition.TABLE);
    }

    public <T extends Organition> Organition findOne(List<Filter> filters, Class<T> obj){
        return this.mongoDao.findOne(CommonQueryUtils.getFilters(filters), obj, Organition.TABLE);
    }
}
