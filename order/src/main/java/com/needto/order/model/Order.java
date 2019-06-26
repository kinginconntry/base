package com.needto.order.model;

import com.needto.dao.models.TargetEntity;
import com.needto.discount.entity.DiscountMultiResult;
import com.needto.discount.entity.DiscountUnit;
import com.needto.order.data.Product;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * 订单数据
 */
public class Order extends TargetEntity {

    public static final String TABLE = "_order";

    // 支付订单的基本信息
    /**
     * 订单号
     */
    public String orderNo;
    /**
     * 购买产品
     */
    public List<Product> products;
    /**
     * 订单附加信息
     */
    public String info;
    /**
     * 支付方式、途径：支付宝、微信等
     */
    public String way;
    /**
     * 订单备注， 可选
     */
    public String remark;
    /**
     * 订单状态
     * @see com.needto.order.data.OrderStatus
     */
    public String status;
    /**
     * 订单业务标识
     */
    public String biz;
    /**
     * 整体折扣
     */
    public List<DiscountUnit> discountUnits;


    // 支付完成时的时间（成功或失败）
    /**
     * 订单完成时间
     */
    public Date time;
    // 订单支付成功时的信息
    /**
     * 总金额
     */
    public long sum;
    /**
     * 第三方订单编号，通过此id查询第三方订单数据
     */
    public String thirdOrderCode;
    /**
     * 订单完成时折扣结果
     */
    public DiscountMultiResult discountMultiResult;

    // 订单支付失败时的信息
    /**
     * 支付失败原因
     */
    public String freason;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<DiscountUnit> getDiscountUnits() {
        return discountUnits;
    }

    public void setDiscountUnits(List<DiscountUnit> discountUnits) {
        this.discountUnits = discountUnits;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBiz() {
        return biz;
    }

    public void setBiz(String biz) {
        this.biz = biz;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public String getThirdOrderCode() {
        return thirdOrderCode;
    }

    public void setThirdOrderCode(String thirdOrderCode) {
        this.thirdOrderCode = thirdOrderCode;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public DiscountMultiResult getDiscountMultiResult() {
        return discountMultiResult;
    }

    public void setDiscountMultiResult(DiscountMultiResult discountMultiResult) {
        this.discountMultiResult = discountMultiResult;
    }

    public String getFreason() {
        return freason;
    }

    public void setFreason(String freason) {
        this.freason = freason;
    }
}
