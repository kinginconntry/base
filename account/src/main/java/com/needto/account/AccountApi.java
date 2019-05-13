package com.needto.account;

import com.needto.account.model.Account;
import com.needto.account.service.AccountService;
import com.needto.common.context.GlobalEnv;
import com.needto.common.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * 默认的用户共用配置信息
 */
@RestController
public class AccountApi {

    @Autowired
    private AccountService accountService;
    
    /**
     * 查找账户数据
     * @return
     */
    @PostMapping(value = {"/app/account/find", "/sys/account/find", "/admin/account/find"})
    @ResponseBody
    public Result<Account> findAccount(){
        return Result.forSuccess(accountService.find(GlobalEnv.getOwner()));
    }

}
