package com.needto.thirdauth.model;

import com.needto.dao.models.BaseEntity;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 */
public class AccessToken extends BaseEntity {

    public static final String TABLE = "_thirdAccessToken";
    /**
     * 本地id
     */
    private String localId;

    /**
     * 类型
     */
    private String type;

    /**
     * token
     */
    private String accessToken;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * token过期时间
     */
    private Long expire;

    /**
     * 刷新tonken过期时间
     */
    private Long refreshExpire;

    /**
     * 额外数据
     */
    private Dict extra;

    public AccessToken(String type) {
        this.type = type;
        this.extra = new Dict();
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Dict getExtra() {
        return extra;
    }

    public void setExtra(Dict extra) {
        this.extra = extra;
    }

    public Long getRefreshExpire() {
        return refreshExpire;
    }

    public void setRefreshExpire(Long refreshExpire) {
        this.refreshExpire = refreshExpire;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void addExtra(String key, Object object){
        if(StringUtils.isEmpty(key)){
            return;
        }
        this.extra.put(key, object);
    }
}
