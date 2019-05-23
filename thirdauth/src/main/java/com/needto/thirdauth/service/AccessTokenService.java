package com.needto.thirdauth.service;

import com.google.common.collect.Lists;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.FieldFilter;
import com.needto.thirdauth.model.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * 第三方token信息
 */
@Service
public class AccessTokenService {

    @Autowired
    private CommonDao commonDao;

    /**
     * 保存token信息
     * @param token
     * @return
     */
    public AccessToken save(AccessToken token){
        return commonDao.save(token, AccessToken.TABLE);
    }

    /**
     * 通过guid查找token信息
     * @param guid
     * @return
     */
    public AccessToken findOneByGuid(String guid){
        return commonDao.findOne(FieldFilter.single("guid", guid), AccessToken.class, AccessToken.TABLE);
    }

    /**
     * 获取用户相应的第三方token信息
     * @param way
     * @param userId
     * @return
     */
    public AccessToken findOneByUserId(String way, String userId){
        return commonDao.findOne(Lists.newArrayList(
                new FieldFilter("type", way),
                new FieldFilter("userId", userId)
        ), AccessToken.class, AccessToken.TABLE);
    }
}
