package com.needto.order.data;

/**
 * @author Administrator
 * 订单状态
 */
public enum OrderStatus {
    /**
     * 待支付
     */
    NEEDPAY(0),
    /**
     * 支付失败
     */
    PAYFAILED(2),
    /**
     * 支付成功
     */
    SUCCESS(3);

    public int key;

    OrderStatus(int key){
        this.key = key;
    }

    public static boolean contain(int key){
        for(OrderStatus status : OrderStatus.values()){
            if(status.key == key){
                return true;
            }
        }
        return false;
    }
}
