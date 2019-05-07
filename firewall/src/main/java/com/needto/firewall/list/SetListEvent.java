package com.needto.firewall.list;

import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 设置黑名单事件
 */
public class SetListEvent extends ApplicationEvent {

    /**
     * 被设置的黑名单
     */
    private FilterList filterList;

    public SetListEvent(Object source, FilterList filterList) {
        super(source);
        this.filterList = filterList;
    }

    public FilterList getFilterList() {
        return filterList;
    }

    public void setFilterList(FilterList filterList) {
        this.filterList = filterList;
    }
}
