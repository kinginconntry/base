package com.needto.order.controller;

import com.needto.discount.entity.DiscountMultiResult;
import com.needto.order.data.OrderStatus;
import com.needto.order.model.Order;
import com.needto.order.service.OrderService;
import com.needto.tool.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

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
    public Result<DiscountMultiResult> discount(@RequestBody Order order){
        return Result.forSuccessIfNotNull(orderService.discount(order, null));
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
            // 没有查到订单
            return Result.forError("NO_ORDER", "");
        }

        if(OrderStatus.SUCCESS.key.equals(order.getStatus())){
            // 支付成功
            return Result.forSuccess(true);
        }else if(OrderStatus.PAYFAILED.key.equals(order.getStatus())){
            // 支付失败
            Result<Boolean> res = new Result<>();
            res.setData(false);
            res.setMessage(order.getFreason());
            res.setSuccess(true);
            return res;
        }else if(OrderStatus.NEEDPAY.key.equals(order.getStatus())){
            // 还未支付
            return Result.forSuccess(false);
        }else{
            // 支付出现问题
            return Result.forError();
        }
    }
}
