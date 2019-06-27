package com.needto.account;

import com.needto.account.model.Account;
import com.needto.account.model.LevelConfig;
import com.needto.account.model.ValueAddService;
import com.needto.account.service.AccountService;
import com.needto.account.service.AppFeatureServiceManager;
import com.needto.tool.entity.Result;
import com.needto.web.context.WebEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Administrator
 * 默认的用户共用配置信息
 */
@RestController
public class AccountApi {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AppFeatureServiceManager appFeatureServiceManager;
    
    /**
     * 查找账户数据
     * @return
     */
    @RequestMapping(value = {"/app/account/find", "/sys/account/find", "/admin/account/find"})
    @ResponseBody
    public Result<Account> findAccount(){
        return Result.forSuccess(accountService.find(WebEnv.getOwner()));
    }

    /**
     * 获取版本信息
     * @return
     */
    @RequestMapping(value = {"/app/levelconfig/find", "/sys/levelconfig/find", "/admin/levelconfig/find"})
    @ResponseBody
    public Result<List<LevelConfig>> findLevelConfig(){
        return Result.forSuccess(appFeatureServiceManager.getLevelConfig(null, null));
    }

    /**
     * 获取增值服务特性
     * @return
     */
    @RequestMapping(value = {"/app/valueaddservice/find", "/sys/valueaddservice/find", "/admin/valueaddservice/find"})
    @ResponseBody
    public Result<List<ValueAddService>> findValueAddService(){
        return Result.forSuccess(appFeatureServiceManager.getValueAddService(null, null));
    }

}
