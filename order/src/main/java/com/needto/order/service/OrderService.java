package com.needto.order.service;

import com.google.common.collect.Lists;
import com.needto.common.entity.Filter;
import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Target;
import com.needto.common.exception.BaseException;
import com.needto.common.utils.Assert;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.PageDataResult;
import com.needto.discount.service.DiscountService;
import com.needto.discount.service.IDiscount;
import com.needto.order.data.Discount;
import com.needto.order.data.Product;
import com.needto.order.event.OrderAfterSaveEvent;
import com.needto.order.event.OrderBeforeSaveEvent;
import com.needto.order.model.Order;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Administrator
 * 订单服务
 */
@Service
public class OrderService {

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DiscountService discountService;

    /**
     * 创建新订单
     * @param order
     * @return
     */
    public Order create(Order order, Target client){
        Assert.validateNull(order);
        Assert.validateCondition(!StringUtils.isEmpty(order.getId()), "INVALID_ORDER", "");
        return save(order, client);
    }

    private void dealDiscount(Order order){
        for(Product product : order.getProducts()){
            Discount discount = product.getDiscount();
            if(discount != null && !StringUtils.isEmpty(discount.getKey())){
                try{
                    IDiscount iDiscount = discountService.get(discount.getKey());
                    if(iDiscount != null){
                        long total = product.getPrice() * product.getNumber();
                        BigDecimal temp = iDiscount.cal(discount.getKey(), BigDecimal.valueOf(total), discount.getAuth());
                        discount.setSum(total - temp.longValue());
                    }
                }catch (BaseException e){
                    discount.setDisableReason(e.getErrMsg());
                    discount.setEnable(false);
                }
            }
        }
    }

    /**
     * 保存订单
     * @param order
     * @return
     */
    public Order save(Order order, Target client){
        Assert.validateStringEmpty(order.getWay(), "INVALID_WAY", "");

        // 计算折扣金额和总金额
        applicationContext.publishEvent(new OrderBeforeSaveEvent(this, order, client));
        Order temp = this.commonDao.save(order, Order.TABLE);
        applicationContext.publishEvent(new OrderAfterSaveEvent(this, order, client));
        return temp;
    }

    /**
     * 查询某个人的订单信息
     * @param id
     * @param belongto
     * @return
     */
    public Order findById(String id, Target belongto){
        Assert.validateStringEmpty(id);
        Order order = this.commonDao.findOne(Lists.newArrayList(
                new FieldFilter("belongTo.type", belongto.getType()),
                new FieldFilter("belongTo.guid", belongto.getGuid())
        ), Order.class, Order.TABLE);
        if(order == null){
            return null;
        }
        dealDiscount(order);
        return order;
    }

    /**
     * 查询某个订单信息
     * @param id
     * @return
     */
    public Order findById(String id){
        Assert.validateStringEmpty(id);
        Order order = this.commonDao.findById(id, Order.class, Order.TABLE);
        if(order == null){
            return null;
        }
        dealDiscount(order);
        return order;
    }

    /**
     * 查询订单
     * @param query
     * @param belongTo
     * @return
     */
    public PageResult<List<Order>> findByPage(Query query, Target belongTo){
        Assert.validateNull(belongTo);
        if(query == null){
            query = new Query();
        }
        List<Filter> fieldFilters = query.getFilters();
        fieldFilters.add(new Filter("belongTo.type", belongTo.getType()));
        fieldFilters.add(new Filter("belongTo.guid", belongTo.getGuid()));
        query.setFilters(fieldFilters);
        PageDataResult<Order> res = commonDao.findByPage(CommonQueryUtils.getQuery(query), Order.class, Order.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    /**
     * 检查订单
     * @param id
     * @return
     */
    public Order checkById(String id){
        Assert.validateStringEmpty(id);
        return this.commonDao.findOne(FieldFilter.single("id", id), Order.class, Order.TABLE);
    }
}
