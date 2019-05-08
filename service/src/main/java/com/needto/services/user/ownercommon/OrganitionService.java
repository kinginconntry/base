package com.needto.services.user.ownercommon;

import com.needto.common.utils.Assert;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.services.user.ownercommon.entity.Organition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Administrator
 */
@Service
public class OrganitionService {

    @Autowired
    private CommonDao mongoDao;

    public Organition init(Organition organition){
        Assert.validateNull(organition);
        Assert.validateStringEmpty(organition.getOwner());
        Organition old = this.mongoDao.findOne(FieldFilter.single("owner", organition.getOwner()), Organition.class, Organition.TABLE);
        if(old == null){
            organition.setId(null);
            organition = this.mongoDao.save(organition, Organition.TABLE);
        }else{
            organition = old;
        }
        return organition;
    }

    public Organition save(Organition organition){
        Assert.validateNull(organition);
        Assert.validateStringEmpty(organition.getId());
        Assert.validateStringEmpty(organition.getOwner());
        Assert.validateNull(this.mongoDao.findById(organition.getId(), Organition.class, Organition.TABLE));
        return this.mongoDao.save(organition, Organition.TABLE);
    }

    public Organition find(String owner){
        Assert.validateStringEmpty(owner);
        return this.mongoDao.findOne(FieldFilter.single("owner", owner), Organition.class, Organition.TABLE);
    }
}
