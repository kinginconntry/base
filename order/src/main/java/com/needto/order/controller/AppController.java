package com.needto.order.controller;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.order.data.OrderData;
import com.needto.order.data.OrderStatus;
import com.needto.order.model.Order;
import com.needto.order.service.OrderService;
import com.needto.tool.entity.Result;
import com.needto.web.context.WebEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Administrator
 * 下单基础类
 */
@RestController
public class AppController {

    @Autowired
    protected OrderService orderService;

    /**
     * 保存app订单，返回订单信息
     * @param order
     * @return
     */
    @RequestMapping("/app/order/save")
    @ResponseBody
    public Result<OrderData> buy(HttpServletRequest request, @RequestBody Order order){
        // 保存订单
        order.setOwner(WebEnv.getOwner());
        return Result.forSuccessIfNotNull(OrderData.get(orderService.create(order, WebEnv.getClient(request))));
    }

    /**
     * 查询账户的订单
     * @param id
     * @return
     */
    @RequestMapping("/app/order/find/{id}")
    @ResponseBody
    public Result<OrderData> findOne(@PathVariable String id){
        return Result.forSuccessIfNotNull(OrderData.get(orderService.findById(id, WebEnv.getOwnerTarget())));
    }

    /**
     * 查询订单
     * @return
     */
    @RequestMapping("/app/order/page")
    @ResponseBody
    public PageResult<List<Order>> findByPage(@RequestBody Query query){
        return orderService.findByPage(query, WebEnv.getOwnerTarget());
    }

    /**
     * 轮询检查订单是否完成
     * @return
     */
    @RequestMapping("/app/order/isfinish")
    @ResponseBody
    public Result<Boolean> finish(@RequestParam String id){

        Order order = orderService.checkById(id);
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
