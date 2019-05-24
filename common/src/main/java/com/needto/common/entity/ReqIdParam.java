package com.needto.common.entity;


import com.needto.tool.entity.Update;

import java.util.List;

/**
 * @author Administrator
 * http统一更新参数
 */
public class ReqIdParam extends RequestParam {

    public List<String> ids;

    public List<Update> updates;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<Update> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Update> updates) {
        this.updates = updates;
    }
}
