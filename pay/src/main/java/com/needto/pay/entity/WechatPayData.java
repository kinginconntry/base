package com.needto.pay.entity;


import com.needto.common.entity.Dict;

/**
 * @author Administrator
 */
public class WechatPayData extends PayData {

    public enum TradeType {
        JSAPI,
        NATIVE,
        APP;
    }



    private String body;

    private String detail;

    private String attach;

    private String ip;
    /**
     * 支付类型：公众号，小程序，app，普通二维码等
     */
    private String tradeType;

    private String openId;

    private String limitPay;

    private String productId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getLimitPay() {
        return limitPay;
    }

    public void setLimitPay(String limitPay) {
        this.limitPay = limitPay;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
