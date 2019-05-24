package com.needto.cipher.model;


import com.needto.common.entity.Target;
import com.needto.dao.models.UserEntity;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author Administrator
 * api调用凭证信息
 */
public class ApiAccess extends UserEntity {

    public static final String TABLE = "apiaccess";

    /**
     * 所属
     */
    private Target belongto;

    /**
     * 名称
     */
    private String name;

    /**
     * 接入id
     */
    private String accessId;

    /**
     * 密钥
     */
    private String secret;

    /**
     * 加密方式
     */
    private CryptoUtil.Crypto crypto;

    /**
     * 描述
     */
    private String desc;

    /**
     * 可调用范围：若为null，表示没有限制
     */
    private List<String> scopes;

    /**
     * 配置
     */
    private Dict config;

    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Target getBelongto() {
        return belongto;
    }

    public void setBelongto(Target belongto) {
        this.belongto = belongto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public void setScopes(List<String> scopes) {
        this.scopes = scopes;
    }

    public Dict getConfig() {
        return config;
    }

    public void setConfig(Dict config) {
        this.config = config;
    }

    public CryptoUtil.Crypto getCrypto() {
        return crypto == null ? CryptoUtil.Crypto.MD5 : crypto;
    }

    public void setCrypto(String key) {
        if(StringUtils.isEmpty(key)){
            return;
        }
        this.crypto = CryptoUtil.Crypto.getCrypto(key);
    }

    public <T> T getConfig(String key){
        if(StringUtils.isEmpty(key) || this.config == null){
            return null;
        }
        return this.config.getValue(key);
    }

    public void setConfig(String key, Object value){
        Assert.validateStringEmpty(key);
        if(this.config == null){
            this.config = new Dict();
        }
        this.config.put(key, value);
    }
}
