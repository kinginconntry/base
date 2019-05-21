package com.needto.order.data;

import com.needto.common.entity.Target;
import com.needto.order.model.Order;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 订单数据
 */
public class OrderData {

    public String id;

    public Date ctime;

    public String cuser;

    /**
     * 订单号
     */
    public String orderId;

    /**
     * 订单商户
     */
    public Target source;

    /**
     * 订单所属
     */
    public Target belongTo;

    /**
     * 购买产品
     */
    public List<Product> products;

    /**
     * 订单附加信息
     */
    public String info;

    /**
     * 总金额
     */
    public long sum;

    /**
     * 折扣钱
     */
    public long discountSum;

    /**
     * 订单备注， 可选
     */
    public String remark;

    /**
     * 支付方式
     */
    public String way;

    /**
     * 订单状态
     * @see com.needto.order.data.OrderStatus
     */
    public int status;

    /**
     * 订单业务标识
     */
    public String biz;

    /**
     * 备注信息
     */
    public String memo;

    public static OrderData get(Order order){
        if(order == null){
            return null;
        }
        OrderData orderData = new OrderData();
        orderData.setId(order.getId());
        orderData.setBiz(order.getBiz());
        orderData.setCtime(order.getCtime());
        orderData.setInfo(order.getInfo());
        orderData.setOrderId(order.getOrderId());
        orderData.setProducts(order.getProducts());
        orderData.setRemark(order.getRemark());
        orderData.setStatus(order.getStatus());
        orderData.setSum(order.getSum());
        orderData.setWay(order.getWay());
        return orderData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getCuser() {
        return cuser;
    }

    public void setCuser(String cuser) {
        this.cuser = cuser;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Target getSource() {
        return source;
    }

    public void setSource(Target source) {
        this.source = source;
    }

    public Target getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Target belongTo) {
        this.belongTo = belongTo;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public long getDiscountSum() {
        return discountSum;
    }

    public void setDiscountSum(long discountSum) {
        this.discountSum = discountSum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
