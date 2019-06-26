package com.needto.order.service;

import com.needto.common.entity.PageResult;
import com.needto.common.entity.Query;
import com.needto.common.entity.Target;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.PageDataResult;
import com.needto.discount.entity.DiscountMulti;
import com.needto.discount.entity.DiscountMultiResult;
import com.needto.discount.entity.DiscountUnit;
import com.needto.discount.service.DiscountService;
import com.needto.order.data.OrderStatus;
import com.needto.order.data.Product;
import com.needto.order.model.Order;
import com.needto.tool.entity.Dict;
import com.needto.tool.entity.Filter;
import com.needto.tool.utils.Assert;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
    private DiscountService discountService;

    @Resource
    private ProductService productService;

    /**
     * 计算折扣价格
     * @param order
     * @return
     */
    public DiscountMultiResult discount(Order order, Dict config){
        DiscountMulti discountMulti = new DiscountMulti();
        discountMulti.setConfig(config);
        discountMulti.addDiscountUnitList(order.getDiscountUnits());
        if(!CollectionUtils.isEmpty(order.getProducts())){
            order.getProducts().forEach(v -> {
                Product product = productService.getProduct(v.getKey());
                if(product != null && !StringUtils.isEmpty(v.getCode())){
                    DiscountUnit discountUnit = new DiscountUnit();
                    discountUnit.setCode(v.getCode());
                    discountUnit.setConfig(v.getConfig());
                    discountUnit.setFee(new BigDecimal(product.getPrice() * product.getNumber()));
                    discountMulti.addDiscountUnit(discountUnit);
                }
            });
        }
        return discountService.discount(discountMulti);
    }

    /**
     * 创建新订单
     * @param order
     * @return
     */
    public <T extends Order> T create(T order, Target client){
        Assert.validateNull(order);
        Assert.validateCondition(!StringUtils.isEmpty(order.getId()), "INVALID_ORDER", "");
        order.setStatus(OrderStatus.NEEDPAY.key);
        return this.save(order, client);
    }

    /**
     * 保存订单
     * @param order
     * @return
     */
    public <T extends Order> T save(T order, Target client){
        Assert.validateStringEmpty(order.getWay(), "INVALID_WAY", "");
        Assert.validateCondition(CollectionUtils.isEmpty(order.getProducts()), "NO_PRODUCT", "");
        Assert.validateStringEmpty(order.getOrderNo());
        Assert.validateStringEmpty(order.getStatus());

        return this.commonDao.save(order, Order.TABLE);
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
    public <T extends Order> PageResult<List<T>> findByPage(Query query, Class<T> obj){
        PageDataResult<T> res = commonDao.findByPage(CommonQueryUtils.getQuery(query), obj, Order.TABLE);
        return PageResult.forSuccess(res.getTotal(), res.getPage(), res.getData());
    }

    /**
     * 查询订单
     * @param filters
     * @param orders
     * @param obj
     * @param <T>
     * @return
     */
    public <T extends Order> List<T> find(List<Filter> filters, List<com.needto.tool.entity.Order> orders, Class<T> obj){
        return commonDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), obj, Order.TABLE);
    }

    /**
     * 删除
     * @param filters
     * @return
     */
    public long delete(List<Filter> filters){
        return commonDao.delete(CommonQueryUtils.getFilters(filters), Order.TABLE);
    }

    /**
     * 删除打标
     * @param filters
     * @return
     */
    public long updateDelete(List<Filter> filters){
        return commonDao.updateDeleted(CommonQueryUtils.getFilters(filters), Order.TABLE);
    }
}
