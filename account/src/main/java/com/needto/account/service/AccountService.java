package com.needto.account.service;

import com.needto.account.entity.AccountResource;
import com.needto.account.model.Account;
import com.needto.account.model.LevelConfig;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author Administrator
 * 账户服务
 */
@Service
public class AccountService {

    @Autowired
    private AppFeatureServiceManager appFeatureServiceManager;

    @Autowired
    private CommonDao commonDao;

    public Account init(String owner){
        Account account = new Account();
        account.setOwner(owner);
        LevelConfig levelConfig = appFeatureServiceManager.getDefaultLevelConfig();
        if(levelConfig != null){
            Date now = new Date();
            List<LevelConfig.Resource> resources = levelConfig.getResources();
            if(!CollectionUtils.isEmpty(resources)){
                account.setResourceMap(new HashMap<>());
                for(LevelConfig.Resource resource : resources){
                    AccountResource accountResource = new AccountResource();
                    accountResource.setCurrent(0);
                    accountResource.setTotal(resource.max);
                    if(resource.expire != null && resource.expire > 0){
                        accountResource.setStart(now);
                    }
//                    account.getResourceMap().put(resource.key, )
                }
            }
        }
        return this.save(account);
    }

    public Account save(Account account){
        Assert.validateNull(account);
        Assert.validateStringEmpty(account.getOwner());
        if(!StringUtils.isEmpty(account.getId()) && this.commonDao.findOne(FieldFilter.single("owner", account.getOwner()), Account.class, Account.TABLE) != null){
            throw new LogicException("EXISTS_ACCOUNT", "");
        }
        Assert.validateNull(this.commonDao.findById(account.getId(), Account.class, Account.TABLE));
        return this.commonDao.save(account, Account.TABLE);
    }

    public Account find(String owner){
        return this.commonDao.findOne(FieldFilter.single("owner", owner), Account.class, Account.TABLE);
    }
}
