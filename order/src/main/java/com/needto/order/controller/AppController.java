package com.needto.order.controller;

import com.needto.order.data.DiscountData;
import com.needto.order.data.OrderStatus;
import com.needto.order.model.Order;
import com.needto.order.service.OrderService;
import com.needto.tool.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author Administrator
 * 下单基础类
 */
@RestController
public class AppController {

    @Autowired
    protected OrderService orderService;

    /**
     * 获取折扣
     * @return
     */
    @RequestMapping("/app/order/discount/prepare")
    @ResponseBody
    public Result<BigDecimal> discount(@RequestBody DiscountData discountData){
        return orderService.discount(discountData.discountConfig, discountData.fee);
    }

    /**
     * 轮询检查订单是否完成
     * @return
     */
    @RequestMapping("/app/order/isfinish")
    @ResponseBody
    public Result<Boolean> finish(@RequestParam String id){

        Order order = orderService.findById(id, Order.class);
        if(order == null){
            return Result.forError("NO_ORDER", "");
        }

        if(OrderStatus.SUCCESS.key == order.getStatus()){
            // 支付成功
            return Result.forSuccess(true);
        }else if(OrderStatus.PAYFAILED.key == order.getStatus()){
            // 支付失败
            Result<Boolean> res = new Result<>();
            res.setData(false);
            res.setMessage(order.getFreason());
            return res;
        }else{
            return Result.forSuccess(false);
        }
    }
}
