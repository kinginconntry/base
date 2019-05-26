package com.needto.pay.config;

import com.needto.tool.utils.Assert;
import com.needto.tool.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 */
@ConfigurationProperties(prefix="pay.alipay")
public class AlipayConfig {

    @Autowired
    private ApplicationContext applicationContext;

    private String appId;

    private String privateKey;

    private String publicKey;

    private String domain;

    private String notifyUrl;

    @PostConstruct
    private void init(){
        Assert.validateStringEmpty(this.getAppId());
        Assert.validateStringEmpty(this.getPrivateKey());
        Assert.validateStringEmpty(this.getPublicKey());
        if(StringUtils.isEmpty(this.getNotifyUrl())){
            this.setNotifyUrl(this.getDomain() + "/pay/alipay/notify");
        }
        Assert.validateCondition(ValidateUtils.isUrl(this.getNotifyUrl()));
        applicationContext.getAutowireCapableBeanFactory().createBean(AlipayOpen.class);

    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
}
