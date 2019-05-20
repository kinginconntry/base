package com.needto.pay.entity;


/**
 * @author Administrator
 */
public class AlipayData extends PayData {

    public enum IntegrationType{
        ALIAPP,
        PCWEB;
    }
    private String subject;
    private String productCode;
    private String body;
    private String integrationType;


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    public void setIntegrationType(String integrationType) {
        this.integrationType = integrationType;
    }
}
