package com.needto.pay.config;

import com.needto.common.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

/**
 * @author Administrator
 */
@ConfigurationProperties(prefix="pay.wechat")
public class WechatConfig {

    @Autowired
    private ApplicationContext applicationContext;

    private String appid;
    private String muchId;

    private String notifyUrl;

    private String domain;

    @PostConstruct
    private void init(){
        Assert.validateStringEmpty(this.getAppid());
        Assert.validateStringEmpty(this.getMuchId());

        if(StringUtils.isEmpty(this.getNotifyUrl())){
            this.setNotifyUrl(this.getDomain() + "/pay/wechat/notify");
        }
        applicationContext.getAutowireCapableBeanFactory().createBean(WechatOpen.class);
    }


    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMuchId() {
        return muchId;
    }

    public void setMuchId(String muchId) {
        this.muchId = muchId;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
