package com.needto.common.dao.common;



import java.util.List;

/**
 * 控制层接受的参数
 * 控制层和服务层之间的参数传递
 * @author Administrator
 */
public class CommonQuery {

    public static final Integer DEFAULT_SKIP = 0;

    public static final Integer DEFAULT_LIMIT = 10;

    public Integer skip;

    public Integer limit;

    /**
     * 过滤信息
     */
    public List<FieldFilter> filters;

    /**
     * 排序信息
     */
    public List<FieldOrder> orders;

    /**
     * 需要字段
     */
    public List<String> fields;

    public CommonQuery() {
    }

    public CommonQuery(Integer skip, Integer limit, List<FieldFilter> filters, List<FieldOrder> orders, List<String> fields) {
        this.skip = skip;
        this.limit = limit;
        this.filters = filters;
        this.orders = orders;
        this.fields = fields;
    }

    public void setDefaultSkipAndLimit(){
        this.skip = DEFAULT_SKIP;
        this.limit = DEFAULT_LIMIT;
    }

    public List<FieldFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<FieldFilter> filters) {
        this.filters = filters;
    }

    public List<FieldOrder> getOrders() {
        return orders;
    }

    public void setOrders(List<FieldOrder> orders) {
        this.orders = orders;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public void setOrder(String field, int order) {
        this.orders.add(new FieldOrder(field, order));
    }

    public Integer getSkip() {
        return skip;
    }

    public void setSkip(Integer skip) {
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    /**
     * 是否分页
     * @return
     */
    public boolean isPage(){
        if(this.skip == null || this.limit == null){
            return false;
        }else{
            return true;
        }
    }
}
