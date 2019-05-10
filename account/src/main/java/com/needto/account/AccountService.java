package com.needto.account;

import com.needto.common.utils.Assert;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.FieldFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 账户服务
 */
@Service
public class AccountService {

    @Autowired
    private CommonDao mongoDao;

    public Account init(Account account){
        Assert.validateNull(account);
        Assert.validateStringEmpty(account.getOwner());
        Account old = this.mongoDao.findOne(FieldFilter.single("owner", account.getOwner()), Account.class, Account.TABLE);
        if(old == null){
            account.setId(null);
            account = this.mongoDao.save(account, Account.TABLE);
        }else{
            account = old;
        }
        return account;
    }

    public Account save(Account account){
        Assert.validateNull(account);
        Assert.validateStringEmpty(account.getId());
        Assert.validateStringEmpty(account.getOwner());
        Assert.validateNull(this.mongoDao.findById(account.getId(), Account.class, Account.TABLE));
        return this.mongoDao.save(account, Account.TABLE);
    }

    public Account find(String owner){
        return this.mongoDao.findOne(FieldFilter.single("owner", owner), Account.class, Account.TABLE);
    }
}
