package com.needto.cipher.service;

import com.google.common.collect.Lists;
import com.needto.cipher.model.ApiAccess;
import com.needto.common.entity.Target;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.FieldUpdate;
import com.needto.dao.inter.CommonDao;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 目标api调用
 */
@Service
public class ApiAccessService {


    @Resource
    private CommonDao commonDao;

    public ApiAccess create(ApiAccess apiAccess){
        Assert.validateNull(apiAccess);
        Assert.validateNull(apiAccess.getBelongto());
        Assert.validateStringEmpty(apiAccess.getBelongto().getType());
        Assert.validateStringEmpty(apiAccess.getBelongto().getGuid());
        apiAccess.setAccessId(Utils.getGuid());
        apiAccess.setSecret(Utils.getGuidBase64());
        return this.commonDao.save(apiAccess, ApiAccess.TABLE);
    }

    public long updateBase(Target belongto, String accessId, String name, String desc){
        Assert.validateCondition(StringUtils.isEmpty(accessId) || belongto == null || StringUtils.isEmpty(belongto.getGuid()) || StringUtils.isEmpty(belongto.getType()));
        return this.commonDao.updateOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId),
                new FieldFilter("belongto.type", belongto.getType()),
                new FieldFilter("belongto.guid", belongto.getGuid())
        ), Lists.newArrayList(
                new FieldUpdate("name", name),
                new FieldUpdate("name", name)
        ), ApiAccess.TABLE);
    }

    public long updateConfig(Target belongto, String accessId, Dict config){
        Assert.validateCondition(StringUtils.isEmpty(accessId) || belongto == null || StringUtils.isEmpty(belongto.getGuid()) || StringUtils.isEmpty(belongto.getType()));
        return this.commonDao.updateOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId),
                new FieldFilter("belongto.type", belongto.getType()),
                new FieldFilter("belongto.guid", belongto.getGuid())
        ), FieldUpdate.single("config", config), ApiAccess.TABLE);
    }

    public long updateScopes(Target belongto, String accessId, List<String> scopes){
        Assert.validateCondition(StringUtils.isEmpty(accessId) || belongto == null || StringUtils.isEmpty(belongto.getGuid()) || StringUtils.isEmpty(belongto.getType()));
        return this.commonDao.updateOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId),
                new FieldFilter("belongto.type", belongto.getType()),
                new FieldFilter("belongto.guid", belongto.getGuid())
        ), FieldUpdate.single("scopes", scopes), ApiAccess.TABLE);
    }

    public ApiAccess findOneByAccessId(Target belongto, String accessId){
        if(StringUtils.isEmpty(accessId) || belongto == null || StringUtils.isEmpty(belongto.getGuid()) || StringUtils.isEmpty(belongto.getType())){
            return null;
        }
        return commonDao.findOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId),
                new FieldFilter("belongto.type", belongto.getType()),
                new FieldFilter("belongto.guid", belongto.getGuid())
        ), ApiAccess.class, ApiAccess.TABLE);
    }

    public ApiAccess findOneByAccessId(String accessId){
        if(StringUtils.isEmpty(accessId)){
            return null;
        }
        return commonDao.findOne(Lists.newArrayList(
                new FieldFilter("accessId", accessId)
        ), ApiAccess.class, ApiAccess.TABLE);
    }

    public long deleteApiAccessByAccessId(Target belongto, String accessId){
        if(StringUtils.isEmpty(accessId) || belongto == null || StringUtils.isEmpty(belongto.getGuid()) || StringUtils.isEmpty(belongto.getType())){
            return 0;
        }
        return this.commonDao.delete(Lists.newArrayList(
                new FieldFilter("accessId", accessId),
                new FieldFilter("belongto.type", belongto.getType()),
                new FieldFilter("belongto.guid", belongto.getGuid())
        ), ApiAccess.TABLE);
    }

    public boolean validate(String accessId, Map<String, Object> attrs, String sign){
        if(StringUtils.isEmpty(accessId) || StringUtils.isEmpty(sign)){
            return false;
        }
        ApiAccess apiAccess = this.findOneByAccessId(accessId);
        if(apiAccess == null){
            return false;
        }

        if(attrs == null){
            attrs = new HashMap<>();
        }
        attrs.put("accessId", accessId);
        String sg = SignUtils.getParamSign(attrs, apiAccess.getSecret(), apiAccess.getCrypto().key);
        return sign.equals(sg);
    }
}
