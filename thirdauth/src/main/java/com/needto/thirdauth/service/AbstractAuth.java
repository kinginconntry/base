package com.needto.thirdauth.service;

import com.needto.thirdauth.data.GuidData;
import com.needto.thirdauth.data.ThirdEvent;
import com.needto.thirdauth.data.ThirdAuth;
import com.needto.thirdauth.event.AuthorizeEvent;
import com.needto.thirdauth.event.ThirdChangeEvent;
import com.needto.thirdauth.model.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * @author Administrator
 */
abstract public class AbstractAuth implements IAuth {

    @Autowired
    protected AccessTokenService accessTokenService;

    @Autowired
    protected ThirdGuidService thirdGuidService;

    @Autowired
    protected ApplicationContext applicationContext;

    @Override
    public void authorize(ThirdAuth params) {
        /**
         * 检查是否有该guid，若没有则为非法用户
         * 获取授权第三方唯一id
         * 查找用户表是否有该用户，若没有则创建新用户
         * 初始化完成发送系统事件
         */
        GuidData guidData = thirdGuidService.get(params.getGuid());
        if(guidData == null){
            return;
        }
        String user = this.getLocalId(params);
        if(user == null){
            user = this.createLocal(params);
        }
        guidData.setLocalId(user);
        thirdGuidService.save(guidData);
        applicationContext.publishEvent(new AuthorizeEvent(this, params, user));
    }

    /**
     * 创建本地数据
     * @param params
     * @return
     */
    abstract protected String createLocal(ThirdAuth params);

    @Override
    public AccessToken getAuthorize(String userId) {
        return accessTokenService.findOneByUserId(this.getCode(), userId);
    }

    @Override
    public void event(ThirdEvent event) {
        applicationContext.publishEvent(new ThirdChangeEvent(this, event));
    }
}
