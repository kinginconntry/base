package com.needto.log.entity;

import com.needto.dao.models.BaseEntity;

/**
 * @author Administrator
 * 日志
 */
public class Log extends BaseEntity {

    public String content;

    public String label;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
