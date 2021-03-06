package com.needto.webhook;

import com.needto.dao.models.BaseEntity;
import com.needto.tool.entity.Dict;
import com.needto.tool.utils.CryptoUtil;
import org.springframework.util.StringUtils;

/**
 * @author Administrator
 */
public class WebHook extends BaseEntity {

    public static final String TABLE = "_webhook";

    /**
     * 事件名
     */
    public String event;

    /**
     * 远程url
     */
    private String url;

    /**
     * 服务器配置的headers信息
     */
    private Dict headers;

    /**
     * 是否需要进行参数签名
     */
    private boolean sign;

    /**
     * 签名方式：md5，rsa
     */
    private String signType;

    /**
     * 签名所用的key
     */
    private String signKey;

    /**
     * 数据是否要加密
     */
    private boolean encry;

    /**
     * 数据加密方式
     */
    private String encryType;

    /**
     * 数据加密所用的key
     */
    private String encryKey;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Dict getHeaders() {
        return headers;
    }

    public void setHeaders(Dict headers) {
        this.headers = headers;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return signType;
    }

    public CryptoUtil.Crypto getSignTypeCrypto() {
        if(CryptoUtil.Crypto.contain(this.signType)){
            return CryptoUtil.Crypto.valueOf(signType);
        }
        return null;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public boolean isEncry() {
        return encry;
    }

    public void setEncry(boolean encry) {
        this.encry = encry;
    }

    public String getEncryType() {
        return encryType;
    }

    public CryptoUtil.Crypto getEncryTypeCrypto() {
        if(CryptoUtil.Crypto.contain(this.encryType)){
            return CryptoUtil.Crypto.valueOf(encryType);
        }
        return null;
    }

    public void setEncryType(String encryType) {
        this.encryType = encryType;
    }

    public String getEncryKey() {
        return encryKey;
    }

    public void setEncryKey(String encryKey) {
        this.encryKey = encryKey;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public boolean encryValid(){
        if(this.isEncry() && CryptoUtil.Crypto.contain(this.encryType) && !StringUtils.isEmpty(this.encryKey)){
            return true;
        }else{
            return false;
        }
    }

    public boolean signValid(){
        if(this.isSign() && CryptoUtil.Crypto.contain(this.signType) && !StringUtils.isEmpty(this.signKey)){
            return true;
        }else{
            return false;
        }
    }
}
