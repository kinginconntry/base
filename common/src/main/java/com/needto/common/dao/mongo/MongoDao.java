package com.needto.common.dao.mongo;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.needto.common.dao.common.*;
import com.needto.common.dao.models.BaseEntity;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Administrator
 */
@Component
public class MongoDao implements CommonDao {

    private static final Logger LOG = LoggerFactory.getLogger(MongoDao.class);


    public static final transient String OID = "_id";

    /**
     * NO_UPDATE_FIELDS
     */
    private static final List<String> NO_UPDATE_FIELDS = new ArrayList<>();

    static {
        NO_UPDATE_FIELDS.add(MongoDao.OID);
        NO_UPDATE_FIELDS.add(BaseEntity.CONFUSE_ID);
        NO_UPDATE_FIELDS.add(BaseEntity.CTIME);
        NO_UPDATE_FIELDS.add(BaseEntity.UPTIME);
    }


    public void updateInit(List<FieldUpdate> updates) {
        updates.removeIf(update -> NO_UPDATE_FIELDS.contains(update.getField()));
        updates.add(new FieldUpdate(BaseEntity.UPTIME, new Date()));
    }


    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public <T> T findOne(List<FieldFilter> fieldFilters, List<String> fields, Class<T> objClass, String table) {
        Query query = MongoQueryUtils.getQuery(fieldFilters, fields);
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.findOne(query, objClass, table);
    }


    @Override
    public <T> T findById(String id, List<String> fields, Class<T> objClass, String table) {
        if (!ObjectId.isValid(id)) {
            return null;
        }
        Query query;
        Document document = MongoQueryUtils.getFields(fields);
        if (document == null) {
            query = new Query(Criteria.where(OID).is(new ObjectId(id)));
        } else {
            query = new BasicQuery(Criteria.where(OID).is(new ObjectId(id)).getCriteriaObject(), document);
        }
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.findOne(query, objClass, table);
    }

    @Override
    public <T> T findOne(String query, Class<T> objClass, String table){
        Query queryObj;
        if(StringUtils.isEmpty(query)){
            queryObj = new Query();
        }else{
            queryObj = new BasicQuery(query);
        }
        return this.mongoTemplate.findOne(queryObj, objClass, table);
    }

    @Override
    public <T> List<T> find(String query, Class<T> objClass, String table) {
        Query queryObj;
        if(StringUtils.isEmpty(query)){
            queryObj = new Query();
        }else{
            queryObj = new BasicQuery(query);
        }
        return this.mongoTemplate.find(queryObj, objClass, table);
    }

    @Override
    public <T> T findOneByField(String field, String op, Object value, Class<T> objClass, String table) {
        if(StringUtils.isEmpty(op)){
            op = MongoQueryUtils.MongoOp.EQ.name();
        }
        Document document = new Document();
        document.put(field, new Document(op, value));
        Query query = new BasicQuery(document);
        return this.mongoTemplate.findOne(query, objClass, table);
    }

    @Override
    public <T> List<T> findByIds(List<String> ids, List<String> fields, List<FieldOrder> sorts, Class<T> objClass, String table) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>(0);
        }
        Query query;
        Document document = MongoQueryUtils.getFields(fields);
        if (document == null) {
            query = new Query(Criteria.where(OID).in(MongoQueryUtils.getIds(ids)));
        } else {
            query = new BasicQuery(Criteria.where(OID).in(MongoQueryUtils.getIds(ids)).getCriteriaObject(), document);
        }
        Sort sort = MongoQueryUtils.getSorts(sorts);
        if (sort != null) {
            query.with(sort);
        }
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.find(query, objClass, table);
    }

    public <T> List<T> findByConfuseIds(List<String> confuseIds, Class<T> objClass, String table) {
        return this.mongoTemplate.find(new Query(Criteria.where(BaseEntity.CONFUSE_ID).in(confuseIds)), objClass, table);
    }

    public <T> List<T> find(Query query, Class<T> objClass, String table) {
        return this.mongoTemplate.find(query, objClass, table);
    }

    public <T> T findOne(Query query, Class<T> objClass, String table) {
        return this.mongoTemplate.findOne(query, objClass, table);
    }

    @Override
    public long count(List<FieldFilter> fieldFilters, String table) {
        Query query = MongoQueryUtils.getQuery(fieldFilters);
        LOG.debug("count计算 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.count(query, table);
    }

    @Override
    public long countByIds(List<String> ids, String table) {
        Query query = new Query(Criteria.where(OID).in(MongoQueryUtils.getIds(ids)));
        LOG.debug("count计算 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.count(query, table);
    }

    public long countByConfuseIds(List<String> confuseIds, String table) {
        return this.mongoTemplate.count(new Query(Criteria.where(BaseEntity.CONFUSE_ID).in(confuseIds)), table);
    }

    public long countByQuery(Query query, String table) {
        return this.mongoTemplate.count(query, table);
    }

    @Override
    public <T> PageDataResult<T> findByPage(CommonQuery query, Class<T> objClass, String table) {
        if (!query.isPage()) {
            query.setDefaultSkipAndLimit();
        }
        long count = this.count(query.getFilters(), table);
        Query queryObj = MongoQueryUtils.getQuery(query);
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + queryObj.toString());
        List<T> data = this.mongoTemplate.find(queryObj, objClass, table);
        PageDataResult pageResult = new PageDataResult();
        pageResult.setData(data);
        pageResult.setPage((query.skip / query.limit) + 1);
        pageResult.setSize(query.limit);
        pageResult.setTotal(count);
        return pageResult;
    }

    @Override
    public <T> List<T> find(CommonQuery query, Class<T> objClass, String table) {
        Query queryObj = MongoQueryUtils.getQuery(query);
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + queryObj.toString());
        return this.mongoTemplate.find(queryObj, objClass, table);
    }

    @Override
    public <T> List<T> find(List<FieldFilter> fieldFilters, List<String> fields, List<FieldOrder> sorts, Class<T> objClass, String table) {
        Query query = MongoQueryUtils.getQuery(fieldFilters, fields);
        Sort sort = MongoQueryUtils.getSorts(sorts);
        if (sort != null) {
            query.with(sort);
        }
        LOG.debug("查询数据 --> 表：" + table + "，查询条件：" + query.toString());
        return this.mongoTemplate.find(query, objClass, table);
    }

    @Override
    public <T> T insertOne(T object, String table) {
        if (object == null) {
            return null;
        }
        LOG.debug("插入数据 --> 表：" + table + "，数据：" + object.toString());
        this.mongoTemplate.insert(object, table);
        LOG.debug("插入数据 --> 表：" + table + "，结果：" + object.toString());
        return object;
    }

    @Override
    public <T> Collection<? extends T> insert(List<T> objs, String table) {
        LOG.debug("批量插入数据 --> 表：" + table + "，数据：" + objs.toString());
        this.mongoTemplate.insert(objs, table);
        LOG.debug("批量插入数据 --> 表：" + table + "，结果：" + objs.toString());
        return objs;
    }

    @Override
    public <T> T save(T object, String table) {
        LOG.debug("保存数据 --> 表：" + table + "，数据：" + object.toString());
        this.mongoTemplate.save(object, table);
        LOG.debug("保存数据 --> 表：" + table + "，结果：" + object.toString());
        return object;
    }

    @Override
    public long delete(List<FieldFilter> fieldFilters, String table) {
        if (CollectionUtils.isEmpty(fieldFilters)) {
            return 0;
        }
        Query query = MongoQueryUtils.getQuery(fieldFilters);
        LOG.debug("删除记录 --> 表：" + table + "，过滤条件：" + query.toString());
        DeleteResult deleteResult = this.mongoTemplate.remove(query, table);
        LOG.debug("删除记录 --> 表：" + table + "，结果：" + deleteResult.toString());
        return deleteResult.getDeletedCount();
    }

    /**
     * 更新删除标识
     *
     * @param fieldFilters
     * @param table
     * @return
     */
    public long updateDeleted(List<FieldFilter> fieldFilters, String table) {
        return this.update(fieldFilters, Lists.newArrayList(new FieldUpdate(BaseEntity.DELETED, true)), table);
    }

    @Override
    public long deleteById(String id, String table) {
        if (!ObjectId.isValid(id)) {
            return 0;
        }
        Query query = new Query(Criteria.where(OID).is(new ObjectId(id)));
        LOG.debug("删除记录 --> 表：" + table + "，过滤条件：" + query.toString());
        DeleteResult deleteResult = this.mongoTemplate.remove(query, table);
        LOG.debug("删除记录 --> 表：" + table + "，结果：" + deleteResult.toString());
        return deleteResult.getDeletedCount();
    }

    /**
     * 根据id更新删除标识
     *
     * @param id
     * @param table
     * @return
     */
    public long updateDeletedById(String id, String table) {
        return this.updateById(id, Lists.newArrayList(new FieldUpdate(BaseEntity.DELETED, true)), table);
    }

    public long deleteByConfuseId(String confuseId, String table) {
        DeleteResult deleteResult = this.mongoTemplate.remove(new Query(Criteria.where(BaseEntity.CONFUSE_ID).is(confuseId)), table);
        return deleteResult.getDeletedCount();
    }

    /**
     * 根据混淆id更新删除标识
     *
     * @param confuseId
     * @param table
     * @return
     */
    public long updateDeletedByConfuseId(String confuseId, String table) {
        return this.updateByConfuseId(confuseId, Lists.newArrayList(new FieldUpdate(BaseEntity.DELETED, true)), table);
    }

    @Override
    public long deleteByIds(List<String> ids, String table) {
        if (CollectionUtils.isEmpty(ids)) {
            return 0;
        }
        Query query = new Query(Criteria.where(OID).in(MongoQueryUtils.getIds(ids)));
        LOG.debug("删除记录 --> 表：" + table + "，过滤条件：" + query.toString());
        DeleteResult deleteResult = this.mongoTemplate.remove(query, table);
        LOG.debug("删除记录 --> 表：" + table + "，结果：" + deleteResult.toString());
        return deleteResult.getDeletedCount();
    }

    public long updateDeletedByIds(List<String> ids, String table) {
        return this.updateByIds(ids, Lists.newArrayList(new FieldUpdate(BaseEntity.DELETED, true)), table);
    }

    public long deleteByConfuseIds(List<String> confuseIds, String table) {
        DeleteResult deleteResult = this.mongoTemplate.remove(new Query(Criteria.where(BaseEntity.CONFUSE_ID).in(confuseIds)), table);
        return deleteResult.getDeletedCount();
    }

    public long updateDeletedByConfuseIds(List<String> confuseIds, String table) {
        return this.updateByConfuseIds(confuseIds, Lists.newArrayList(new FieldUpdate(BaseEntity.DELETED, true)), table);
    }


    @Override
    public long updateOne(List<FieldFilter> fieldFilters, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(fieldFilters) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        Query query = MongoQueryUtils.getQuery(fieldFilters);
        Update update = MongoQueryUtils.getUpdate(updates);
        LOG.debug("更新记录 --> 表：" + table + "，过滤条件：" + query.toString() + ",更新数据：" + update.toString());
        UpdateResult updateResult = this.mongoTemplate.updateFirst(query, update, table);
        LOG.debug("更新记录 --> 表：" + table + "，结果：" + updateResult.toString());
        return updateResult.getModifiedCount();
    }

    @Override
    public long updateById(String id, List<FieldUpdate> updates, String table) {
        if (StringUtils.isEmpty(id) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        Query query = new Query(Criteria.where(OID).is(new ObjectId(id)));
        Update update = MongoQueryUtils.getUpdate(updates);
        LOG.debug("更新记录 --> 表：" + table + "，过滤条件：" + query.toString() + ",更新数据：" + update.toString());
        UpdateResult updateResult = this.mongoTemplate.updateFirst(query, update, table);
        LOG.debug("更新记录 --> 表：" + table + "，结果：" + updateResult.toString());
        return updateResult.getModifiedCount();
    }

    public long incById(String id, List<FieldUpdate> updates, String table) {
        if (StringUtils.isEmpty(id) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateFirst(new Query(Criteria.where(OID).is(new ObjectId(id))), MongoQueryUtils.getIncUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    public long updateByConfuseId(String confuseId, List<FieldUpdate> updates, String table) {
        if (StringUtils.isEmpty(confuseId) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateFirst(new Query(Criteria.where(BaseEntity.CONFUSE_ID).is(confuseId)), MongoQueryUtils.getUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    public long incByConfuseId(String confuseId, List<FieldUpdate> updates, String table) {
        if (StringUtils.isEmpty(confuseId) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateFirst(new Query(Criteria.where(BaseEntity.CONFUSE_ID).is(confuseId)), MongoQueryUtils.getIncUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    @Override
    public long updateByIds(List<String> ids, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        Query query = new Query(Criteria.where(OID).in(MongoQueryUtils.getIds(ids)));
        Update update = MongoQueryUtils.getUpdate(updates);
        LOG.debug("更新记录 --> 表：" + table + "，过滤条件：" + query.toString() + ",更新数据：" + update.toString());
        UpdateResult updateResult = this.mongoTemplate.updateMulti(query, update, table);
        LOG.debug("更新记录 --> 表：" + table + "，结果：" + updateResult.toString());
        return updateResult.getModifiedCount();
    }

    public long incByIds(List<String> ids, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateMulti(new Query(Criteria.where(OID).in(MongoQueryUtils.getIds(ids))), MongoQueryUtils.getIncUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    public long updateByConfuseIds(List<String> confuseIds, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(confuseIds) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateMulti(new Query(Criteria.where(BaseEntity.CONFUSE_ID).in(confuseIds)), MongoQueryUtils.getUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    public long incByConfuseIds(List<String> confuseIds, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(confuseIds) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        UpdateResult updateResult = this.mongoTemplate.updateMulti(new Query(Criteria.where(BaseEntity.CONFUSE_ID).in(confuseIds)), MongoQueryUtils.getIncUpdate(updates), table);
        return updateResult.getModifiedCount();
    }

    @Override
    public long update(List<FieldFilter> fieldFilters, List<FieldUpdate> updates, String table) {
        if (CollectionUtils.isEmpty(fieldFilters) || CollectionUtils.isEmpty(updates)) {
            return 0;
        }
        updateInit(updates);
        Query query = MongoQueryUtils.getQuery(fieldFilters);
        Update update = MongoQueryUtils.getUpdate(updates);
        LOG.debug("更新记录 --> 表：" + table + "，过滤条件：" + query.toString() + ",更新数据：" + update.toString());
        UpdateResult updateResult = this.mongoTemplate.updateMulti(query, update, table);
        LOG.debug("更新记录 --> 表：" + table + "，结果：" + updateResult.toString());
        return updateResult.getModifiedCount();
    }

    /**
     * 批量修改操作：BulkOperations
     * <p>
     * 批量匹配更新：包含多个批量更新。即可以同时指定多组（匹配条件，更新）的组合,每个组合按照不同的条件更新不同的记录
     *
     * @param collectionName 集合名称(表名)
     * @param matchedUpdates 批量更新的匹配条件和更新
     * @param ordered        如果为true，则如果有一条执行出错就终止；否则会继续执行
     * @see MatchedUpdate
     */
    public void updateBatchMatched(String collectionName, Collection<MatchedUpdate> matchedUpdates, boolean ordered) {
        if (matchedUpdates == null || matchedUpdates.isEmpty()) {
            return;
        }
        BasicDBObject bson = new BasicDBObject(matchedUpdates.size() + 4);
        bson.put("update", collectionName);
        List<BasicDBObject> updates = new ArrayList<>(matchedUpdates.size());
        for (MatchedUpdate one : matchedUpdates) {
            BasicDBObject update = new BasicDBObject();
            update.put("q", one.getMatch());
            update.put("u", one.getUpdate());
            update.put("upsert", one.isUpsert());
            update.put("multi", one.isMulti());
            updates.add(update);
        }
        bson.put("updates", updates);
        bson.put("ordered", ordered);
        LOG.debug("批量更新记录 --> 表：" + collectionName + "，更新数据集：" + bson.toJson());
        // TODO 检查结果
        Document result = mongoTemplate.getDb().runCommand(bson);
        LOG.debug("批量更新记录 --> 结果：" + result.toJson());
    }


}
