package com.needto.account.service;

import com.needto.account.model.Account;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 账户服务
 */
@Service
public class AccountService {

    @Autowired
    private CommonDao commonDao;

    public Account init(Account account){
        Assert.validateNull(account);
        Assert.validateStringEmpty(account.getOwner());
        Account old = this.commonDao.findOne(FieldFilter.single("owner", account.getOwner()), Account.class, Account.TABLE);
        if(old == null){
            account.setId(null);
            account = this.commonDao.save(account, Account.TABLE);
        }else{
            account = old;
        }
        return account;
    }

    public Account save(Account account){
        Assert.validateNull(account);
        Assert.validateStringEmpty(account.getId());
        Assert.validateStringEmpty(account.getOwner());
        Assert.validateNull(this.commonDao.findById(account.getId(), Account.class, Account.TABLE));
        return this.commonDao.save(account, Account.TABLE);
    }

    public Account find(String owner){
        return this.commonDao.findOne(FieldFilter.single("owner", owner), Account.class, Account.TABLE);
    }
}
