package com.needto.thirdauth.service;

import com.needto.thirdauth.data.GuidData;
import com.needto.thirdauth.data.ThirdEvent;
import com.needto.thirdauth.data.ThirdAuth;
import com.needto.thirdauth.model.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThirdService {

    @Autowired
    private AuthRequestContainer authRequestContainer;

    @Autowired
    private ThirdGuidService thirdGuidService;

    public String createGuid(){
        return thirdGuidService.create();
    }

    public GuidData getGuidData(String guid){
        return thirdGuidService.get(guid);
    }

    public void removeGuid(String guid){
        thirdGuidService.remove(guid);
    }

    /**
     * 第三方事件
     * @param param
     */
    public void event(ThirdEvent param){
        IAuth iAuthRequest = authRequestContainer.get(param.getType());
        if(iAuthRequest != null){
            iAuthRequest.event(param);
        }
    }

    /**
     * 用户授权
     * @param userAuth
     */
    public void auth(ThirdAuth userAuth){
        IAuth iAuthRequest = authRequestContainer.get(userAuth.getType());
        if(iAuthRequest != null){
            iAuthRequest.authorize(userAuth);
        }
    }

    /**
     * 获取用户相应的第三方token信息
     * @param way
     * @param userId
     * @return
     */
    public AccessToken getAccessToken(String way, String userId){
        IAuth iAuthRequest = authRequestContainer.get(way);
        if(iAuthRequest != null){
            iAuthRequest.getAuthorize(userId);
        }
        return null;
    }
}
