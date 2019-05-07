package com.needto.services.firewall.list;

import com.needto.common.services.firewall.list.FilterList;
import org.springframework.context.ApplicationEvent;

/**
 * @author Administrator
 * 设置黑名单事件
 */
public class SetListEvent extends ApplicationEvent {

    /**
     * 被设置的黑名单
     */
    private com.needto.common.services.firewall.list.FilterList filterList;

    public SetListEvent(Object source, com.needto.common.services.firewall.list.FilterList filterList) {
        super(source);
        this.filterList = filterList;
    }

    public com.needto.common.services.firewall.list.FilterList getFilterList() {
        return filterList;
    }

    public void setFilterList(FilterList filterList) {
        this.filterList = filterList;
    }
}
