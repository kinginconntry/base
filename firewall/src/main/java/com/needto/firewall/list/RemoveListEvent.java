package com.needto.firewall.list;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author Administrator
 * 移除黑名单事件
 */
public class RemoveListEvent extends ApplicationEvent {

    /**
     * 被移除的黑名单
     */
    private List<FilterList> filterListList;

    public RemoveListEvent(Object source, List<FilterList> filterListList) {
        super(source);
        this.filterListList = filterListList;
    }

    public List<FilterList> getFilterListList() {
        return filterListList;
    }

    public void setFilterListList(List<FilterList> filterListList) {
        this.filterListList = filterListList;
    }
}
