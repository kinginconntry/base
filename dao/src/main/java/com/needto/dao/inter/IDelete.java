package com.needto.dao.inter;

public interface IDelete {

    /**
     * 删除字段
     */
    String DELETED = "deleted";

    Boolean isDeleted();

    void setDeleted(boolean deleted);
}
