package com.needto.common.entity;

import com.needto.tool.entity.Dict;
import com.needto.tool.entity.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * http请求参数
 */
public class RequestParam {
    /**
     * 是否异步执行
     */
    public Boolean isAsync;
    /**
     * 过滤的标准信息
     */
    public List<Filter> filters;

    /**
     * 过滤的额外查询
     */
    public Dict extra;

    /**
     * 过滤的搜索字段
     */
    public String search;

    public List<Filter> getFilters() {
        if(this.filters == null){
            this.filters = new ArrayList<>(0);
        }
        return filters;
    }

    public void setFilters(List<Filter> filters) {
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

    public Boolean isAsync() {
        return isAsync;
    }

    public void setAsync(Boolean async) {
        isAsync = async;
    }
}
