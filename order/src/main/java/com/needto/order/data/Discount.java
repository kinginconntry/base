package com.needto.order.data;

public class Discount {
    /**
     * 折扣配置key
     */
    private String key;

    private String auth;

    /**
     * 折扣是否可用
     */
    private boolean enable;

    /**
     * 折扣不可用原因
     */
    private String disableReason;

    /**
     * 折扣钱
     */
    public long sum;

    public Discount() {
        this.enable = true;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getDisableReason() {
        return disableReason;
    }

    public void setDisableReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
