package com.needto.common.entity;

import com.needto.common.dao.common.FieldUpdate;

import java.util.List;

/**
 * @author Administrator
 */
public class ReqIdParam extends RequestParam {

    /**
     * 是否异步执行
     */
    public Boolean isAsync;

    public List<String> ids;

    public List<FieldUpdate> updates;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public List<FieldUpdate> getUpdates() {
        return updates;
    }

    public void setUpdates(List<FieldUpdate> updates) {
        this.updates = updates;
    }

    public Boolean isAsync() {
        return isAsync;
    }

    public void setAsync(Boolean async) {
        isAsync = async;
    }
}
