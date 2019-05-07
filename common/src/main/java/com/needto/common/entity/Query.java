package com.needto.common.entity;


import java.util.List;

/**
 * @author Administrator
 * 标准查询参数
 */
public class Query extends RequestParam{

    /**
     * 默认最小页码
     */
    private static final Integer DEFAULT_MIN_PAGE = 1;

    /**
     * 默认最小分页条数
     */
    private static final Integer DEFAULT_MIN_SIZE = 1;

    /**
     * 默认最大分页条数
     */
    private static final Integer DEFAULT_MAX_SIZE = 1000;

    /**
     * 分页页码
     */
    public Integer page;

    /**
     * 分页条数
     */
    public Integer size;



    /**
     * 排序信息
     */
    public List<FieldOrder> orders;

    /**
     * 需要字段
     */
    public List<String> fields;


    public Integer getSkip() {
        if(this.page != null && this.size != null){
            if(this.page < DEFAULT_MIN_PAGE){
                this.page = DEFAULT_MIN_PAGE;
            }

            int limit = this.getLimit();
            return (this.page-1)/limit;
        }
        return null;
    }

    public Integer getLimit() {
        if(this.size < DEFAULT_MIN_SIZE){
            this.size = DEFAULT_MIN_SIZE;
        }else if(this.size > DEFAULT_MAX_SIZE){
            this.size = DEFAULT_MAX_SIZE;
        }
        return this.size;
    }

    public boolean isPage(){
        if(this.page == null || this.size == null){
            return false;
        }else{
            return true;
        }
    }



    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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
}
