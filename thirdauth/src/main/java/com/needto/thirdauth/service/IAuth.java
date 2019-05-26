package com.needto.thirdauth.service;

import com.needto.thirdauth.data.ThirdEvent;
import com.needto.thirdauth.data.ThirdAuth;
import com.needto.thirdauth.model.AccessToken;
import com.needto.tool.exception.LogicException;
import com.needto.tool.inter.Thing;

/**
 * @author Administrator
 * 第三方授权接口
 */
public interface IAuth extends Thing {

    /**
     * 用户完成授权，根据授权平台去处理一些授权逻辑，这里需要注册用户信息
     * @param params 由实现类提供类型
     * @return 授权成功，产生一个令牌，通过该令牌调用跟用户相关的所有信息
     */
    void authorize(ThirdAuth params);

    /**
     * 获取本地数据id
     * @param params
     * @return
     */
    String getLocalId(ThirdAuth params);

    /**
     * 根据令牌获取用户授权相关的信息
     * @param localId 本地平台api调用的令牌（token或者sessionid或其他都行，只要能证明客户端身份）
     * @return 本地数据授权相关的信息，方便其他应用直接调用
     */
    default AccessToken getAuthorize(String localId){ throw  new LogicException("NO_AUTH_DATA", ""); }

    /**
     * 事件通知
     * @param event
     */
    default void event(ThirdEvent event){}
}
