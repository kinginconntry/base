package com.needto.ownercommon;


import com.needto.common.entity.Dict;
import com.needto.dao.models.UserEntity;
import com.needto.services.user.ownercommon.entity.AccountResource;
import com.needto.services.user.ownercommon.entity.AccoutFeature;

import java.util.List;
import java.util.Map;

/**
 * 账户实体
 * @author Administrator
 */
public class Account extends UserEntity {

    public static final String TABLE = "_account";

    /**
     * 主用户id
     */
    public String owner;

    /**
     * 成员
     */
    public List<String> members;

    /**
     * 账户特性
     */
    public Map<String, AccoutFeature> featureMap;

    /**
     * 账户资源数
     */
    public Map<String, AccountResource> resourceMap;

    /**
     * 配置信息
     */
    public Dict config;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }

    public Map<String, AccoutFeature> getFeatureMap() {
        return featureMap;
    }

    public void setFeatureMap(Map<String, AccoutFeature> featureMap) {
        this.featureMap = featureMap;
    }

    public Map<String, AccountResource> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map<String, AccountResource> resourceMap) {
        this.resourceMap = resourceMap;
    }
}
