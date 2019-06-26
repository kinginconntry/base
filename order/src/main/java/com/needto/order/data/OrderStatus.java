package com.needto.order.data;

import org.apache.commons.lang.StringUtils;

/**
 * @author Administrator
 * 订单状态
 */
public enum OrderStatus {
    /**
     * 待支付
     */
    NEEDPAY("0"),
    /**
     * 支付失败
     */
    PAYFAILED("1"),
    /**
     * 支付成功
     */
    SUCCESS("2");

    public String key;

    OrderStatus(String key){
        this.key = key;
    }

    public static boolean contain(String key){
        for(OrderStatus status : OrderStatus.values()){
            if(StringUtils.equals(status.key, key)){
                return true;
            }
        }
        return false;
    }
}
