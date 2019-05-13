package com.needto.dao.mongo;

import com.needto.dao.models.BaseEntity;
import org.bson.Document;

/**
 * @author Administrator
 * mongo操作拦截
 */
public interface IMongoIntercept {

    void beforeSave(BaseEntity source, Document document);

    void afterLoad(Document document);
}
