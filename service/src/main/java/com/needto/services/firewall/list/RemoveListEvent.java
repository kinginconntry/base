package com.needto.services.firewall.list;

import com.needto.common.services.firewall.list.FilterList;
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
    private List<com.needto.common.services.firewall.list.FilterList> filterListList;

    public RemoveListEvent(Object source, List<com.needto.common.services.firewall.list.FilterList> filterListList) {
        super(source);
        this.filterListList = filterListList;
    }

    public List<com.needto.common.services.firewall.list.FilterList> getFilterListList() {
        return filterListList;
    }

    public void setFilterListList(List<FilterList> filterListList) {
        this.filterListList = filterListList;
    }
}
