package com.needto.dao.common;

import com.needto.common.entity.Filter;
import com.needto.common.entity.Order;
import com.needto.common.entity.Query;
import com.needto.common.entity.Update;
import com.needto.dao.models.FieldFilter;
import com.needto.dao.models.FieldOrder;
import com.needto.dao.models.FieldUpdate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * dao查询转换基础工具类
 */
public class CommonQueryUtils {

    public static FieldFilter getFilter(Filter filter){
        if(filter == null){
            return null;
        }
        FieldFilter fieldFilter = new FieldFilter();
        fieldFilter.setField(filter.getField());
        fieldFilter.setOp(filter.getOp());
        fieldFilter.setValue(filter.getValue());
        return fieldFilter;
    }

    public static List<FieldFilter> getFilters(List<Filter> filters){
        if(filters == null){
            return null;
        }
        List<FieldFilter> fieldFilters = new ArrayList<>(filters.size());
        filters.forEach(v ->{
            FieldFilter fieldFilter = getFilter(v);
            if(fieldFilter != null){
                fieldFilters.add(fieldFilter);
            }
        });
        return fieldFilters;
    }

    public static FieldOrder getOrder(Order order){
        if(order == null){
            return null;
        }
        FieldOrder fieldOrder = new FieldOrder();
        fieldOrder.setField(order.getField());
        fieldOrder.setOrder(order.getOrder());
        return fieldOrder;
    }

    public static List<FieldOrder> getOrders(List<Order> orders){
        if(orders == null){
            return null;
        }
        List<FieldOrder> fieldOrders = new ArrayList<>(orders.size());
        orders.forEach(v ->{
            FieldOrder fieldOrder = getOrder(v);
            if(fieldOrder != null){
                fieldOrders.add(fieldOrder);
            }
        });
        return fieldOrders;
    }

    public static FieldUpdate getUpdate(Update update){
        if(update == null){
            return null;
        }
        FieldUpdate fieldUpdate = new FieldUpdate();
        fieldUpdate.setField(update.getField());
        fieldUpdate.setValue(update.getValue());
        return fieldUpdate;
    }

    public static List<FieldUpdate> getUpdates(List<Update> updates){
        if(updates == null){
            return null;
        }
        List<FieldUpdate> fieldUpdates = new ArrayList<>(updates.size());
        updates.forEach(v ->{
            FieldUpdate fieldUpdate = getUpdate(v);
            if(fieldUpdate != null){
                fieldUpdates.add(fieldUpdate);
            }
        });
        return fieldUpdates;
    }

    public static CommonQuery getQuery(Query query){
        if(query == null){
            return null;
        }
        CommonQuery commonQuery = new CommonQuery();
        commonQuery.limit = query.getLimit();
        commonQuery.skip = query.getSkip();
        commonQuery.orders = getOrders(query.orders);
        commonQuery.fields = query.fields;
        commonQuery.filters = getFilters(query.filters);
        return commonQuery;
    }


}
