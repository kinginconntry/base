package com.needto.order.model;

import com.needto.common.entity.Dict;
import com.needto.dao.models.TargetEntity;
import com.needto.order.data.Product;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 订单数据
 */
public class Order extends TargetEntity {

    public static final String TABLE = "_order";

    public String owner;

    /**
     * 订单号
     */
    public String orderId;

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
     * 支付方式、途径：支付宝、微信等
     */
    public String way;

    /**
     * 第三方订单编号，通过此id查询第三方订单数据
     */
    public String thirdOrderCode;

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
     * 额外配置信息
     */
    public Dict extra;

    /**
     * 订单完成时间
     */
    public Date ftime;

    /**
     * 支付失败原因
     */
    public String freason;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public long getDiscountSum() {
        return discountSum;
    }

    public void setDiscountSum(long discountSum) {
        this.discountSum = discountSum;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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

    public Dict getExtra() {
        return extra;
    }

    public void setExtra(Dict extra) {
        this.extra = extra;
    }

    public Date getFtime() {
        return ftime;
    }

    public void setFtime(Date ftime) {
        this.ftime = ftime;
    }

    public String getFreason() {
        return freason;
    }

    public void setFreason(String freason) {
        this.freason = freason;
    }

    public String getThirdOrderCode() {
        return thirdOrderCode;
    }

    public void setThirdOrderCode(String thirdOrderCode) {
        this.thirdOrderCode = thirdOrderCode;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
