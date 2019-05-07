package com.needto.common.dao.common;


import java.util.Collection;
import java.util.List;

/**
 * @author Administrator
 * 简单查询
 */
public interface CommonDao {

    long count(List<FieldFilter> fieldFilters, String table);

    long countByIds(List<String> ids, String table);

    <T> T findOne(List<FieldFilter> fieldFilters, List<String> fields, Class<T> objClass, String table);

    default <T> T findOne(List<FieldFilter> fieldFilters, Class<T> objClass, String table) {
        return this.findOne(fieldFilters, null, objClass, table);
    }

    <T> T findOne(String query, Class<T> objClass, String table);

    <T> List<T> find(String query, Class<T> objClass, String table);

    <T> T findOneByField(String field, String op, Object value, Class<T> objClass, String table);

    default <T> T findOneByField(String field, Object value, Class<T> objClass, String table){
        return this.findOneByField(field, null, value, objClass, table);
    }

    <T> T findById(String id, List<String> fields, Class<T> objClass, String table);

    default <T> T findById(String id, Class<T> objClass, String table) {
        return this.findById(id, null, objClass, table);
    }

    <T> List<T> findByIds(List<String> ids, List<String> fields, List<FieldOrder> sorts, Class<T> objClass, String table);

    default <T> List<T> findByIds(List<String> ids, List<FieldOrder> sorts, Class<T> objClass, String table) {
        return this.findByIds(ids, null, sorts, objClass, table);
    }

    default <T> List<T> findByIds(List<String> ids, Class<T> objClass, String table) {
        return this.findByIds(ids, null, objClass, table);
    }

    <T> PageDataResult<T> findByPage(CommonQuery query, Class<T> objClass, String table);

    <T> List<T> find(CommonQuery query, Class<T> objClass, String table);

    <T> List<T> find(List<FieldFilter> fieldFilters, List<String> fields, List<FieldOrder> sorts, Class<T> objClass, String table);

    default <T> List<T> find(List<FieldFilter> fieldFilters, List<FieldOrder> sorts, Class<T> objClass, String table) {
        return this.find(fieldFilters, null, sorts, objClass, table);
    }

    default <T> List<T> find(List<FieldFilter> fieldFilters, Class<T> objClass, String table) {
        return this.find(fieldFilters, null, objClass, table);
    }


    <T> T insertOne(T object, String table);

    <T> Collection<? extends T> insert(List<T> objs, String table);

    <T> T save(T object, String table);

    long delete(List<FieldFilter> fieldFilters, String table);

    long deleteById(String id, String table);

    long deleteByIds(List<String> ids, String table);

    long updateOne(List<FieldFilter> fieldFilters, List<FieldUpdate> updates, String table);

    long updateById(String id, List<FieldUpdate> updates, String table);

    long updateByIds(List<String> ids, List<FieldUpdate> updates, String table);

    long update(List<FieldFilter> fieldFilters, List<FieldUpdate> updates, String table);

}
