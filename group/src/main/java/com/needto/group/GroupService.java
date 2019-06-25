package com.needto.group;

import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.inter.CommonDao;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Order;
import com.needto.tool.entity.TreeData;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * 分组服务
 */
@Service
public class GroupService {

    @Autowired
    private CommonDao mongoDao;

    /**
     * 根据主账户信息与所属获取所有分组
     * @param table
     * @return
     */
    public <T extends Group> List<T> find(List<Filter> filters, List<Order> orders, Class<T> obj, String table){
        return this.mongoDao.find(CommonQueryUtils.getFilters(filters), CommonQueryUtils.getOrders(orders), obj, table);
    }

    /**
     * 获取分组水平结构
     * @param table
     * @return
     */
    public <T extends Group> List<TreeData> getHorizontal(List<Filter> filters, List<Order> orders, Class<T> obj, String table){
        return TreeUtils.getHorizontal(find(filters, orders, obj, table));
    }

    /**
     * 保存分组
     * @param group
     * @param table
     * @return
     */
    public <T extends Group> T save(T group, String table){
        Assert.validateNull(group, "NO_GROUP");
        Assert.validateStringEmpty(group.getName(), "NO_NAME", "");
        return this.mongoDao.save(group, table);
    }

    /**
     * 删除分组
     * @param filters
     * @param table
     * @return
     */
    public Long deleteByIds(List<Filter> filters, String table){
        return this.mongoDao.delete(CommonQueryUtils.getFilters(filters), table);
    }

}
