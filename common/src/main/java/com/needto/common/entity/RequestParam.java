package com.needto.common.entity;

import java.util.List;

/**
 * @author Administrator
 * 请求参数
 */
public class RequestParam {
    /**
     * 过滤的标准信息
     */
    public List<FieldFilter> filters;

    /**
     * 过滤的额外查询
     */
    public Dict extra;

    /**
     * 过滤的搜索字段
     */
    public String search;

    public List<FieldFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<FieldFilter> filters) {
        this.filters = filters;
    }

    public Dict getExtra() {
        return extra;
    }

    public void setExtra(Dict extra) {
        this.extra = extra;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
