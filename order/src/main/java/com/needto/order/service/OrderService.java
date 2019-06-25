package com.needto.order.service;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Target;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.PageDataResult;
import com.needto.discount.entity.DiscountMulti;
import com.needto.discount.entity.DiscountMultiResult;
import com.needto.discount.service.DiscountService;
import com.needto.order.event.OrderAfterSaveEvent;
import com.needto.order.event.OrderBeforeSaveEvent;
import com.needto.order.model.Order;
import com.needto.tool.utils.Assert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
     * 计算折扣价格
     * @param discountConfig
     * @param fee
     * @return
     */
    public DiscountMultiResult discount(DiscountMulti discountConfig, BigDecimal fee){
        return discountService.discount(discountConfig, fee);
    }

    /**
     * 创建新订单
     * @param order
     * @return
     */
    public <T extends Order> T create(T order, Target client){
        Assert.validateNull(order);
        Assert.validateCondition(!StringUtils.isEmpty(order.getId()), "INVALID_ORDER", "");
        return this.save(order, client);
    }

    /**
     * 保存订单
     * @param order
     * @return
     */
    public <T extends Order> T save(T order, Target client){
        Assert.validateStringEmpty(order.getWay(), "INVALID_WAY", "");

        // 计算折扣金额和总金额
        applicationContext.publishEvent(new OrderBeforeSaveEvent(this, order, client));
        T temp = this.commonDao.save(order, Order.TABLE);
        applicationContext.publishEvent(new OrderAfterSaveEvent(this, order, client));
        return temp;
    }


    /**
     * 查询某个订单信息
     * @param id
     * @return
     */
    public <T extends Order> T findById(String id, Class<T> obj){
        Assert.validateStringEmpty(id);
        T order = this.commonDao.findById(id, obj, Order.TABLE);
        if(order == null){
            return null;
        }
        return order;
    }

    /**
     * 查询订单
     * @param query
     * @return
     */
    public PageResult<List<Order>> findByPage(Query query){
        PageDataResult<Order> res = commonDao.findByPage(CommonQueryUtils.getQuery(query), Order.class, Order.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }
}
