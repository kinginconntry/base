package com.needto.common.entity;


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

    public Boolean isAsync() {
        return isAsync;
    }

    public void setAsync(Boolean async) {
        isAsync = async;
    }
}
