package com.needto.common.dao.mongo;

import com.needto.common.dao.common.CommonQuery;
import com.needto.common.dao.common.FieldFilter;
import com.needto.common.dao.common.FieldOrder;
import com.needto.common.dao.common.FieldUpdate;
import com.needto.common.dao.models.BaseEntity;
import com.needto.common.utils.Assert;
import com.needto.common.utils.Utils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;
import org.springframework.util.NumberUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author Administrator
 */
public class MongoQueryUtils {

    /**
     * 内置的需要被转换的字段
     */
    private static final Map<String, String> INNER_FIELD_MAP = new HashMap<>();

    static {
        INNER_FIELD_MAP.put(BaseEntity.ID, MongoDao.OID);
    }

    public static List<ObjectId> getIds(List<String> ids) {
        List<ObjectId> idObj = new ArrayList<>(ids.size());
        ids.forEach((id) -> {
            ObjectId objectId = getId(id);
            if (objectId != null) {
                idObj.add(objectId);
            }
        });
        return idObj;
    }

    /**
     * id字符串转ObjectId
     * @param id
     * @return
     */
    public static ObjectId getId(Object id){
        if(id == null){
            return null;
        }else if(id instanceof ObjectId){
            return (ObjectId) id;
        }else if(id instanceof String && ObjectId.isValid(id.toString())){
            return new ObjectId(id.toString());
        }else{
            throw new IllegalArgumentException("id is illegal");
        }
    }

    /**
     * 支持的mongo操作
     */
    public enum MongoOp {

        /**
         * 大于
         */
        GT,
        /**
         * 大于等于
         */
        GTE,
        /**
         * 小于
         */
        LT,
        /**
         * 小于等于
         */
        LTE,
        /**
         * 存在
         */
        EXISTS,
        /**
         * 在...之中
         */
        IN,
        /**
         * 不在...中
         */
        NIN,
        /**
         * 正则
         */
        REGEX,
        /**
         * 不等于
         */
        NE,
        /**
         *
         */
        EQ
    }

    public static Sort getSorts(List<FieldOrder> orders) {
        if (orders == null) {
            return null;
        }
        List<Sort.Order> orderList = new ArrayList<>(orders.size());
        orders.forEach((order) -> orderList.add(new Sort.Order(order.getOrder() < 0 ? Sort.Direction.DESC : Sort.Direction.ASC, order.getField())));
        return Sort.by(orderList);
    }

    public static Query getQuery(CommonQuery commonQuery){
        if(commonQuery == null){
            return new Query();
        }
        Query query = getQuery(commonQuery.getFilters(), commonQuery.getFields());
        if(!CollectionUtils.isEmpty(commonQuery.getOrders())){
            query.with(MongoQueryUtils.getSorts(commonQuery.getOrders()));
        }
        if(commonQuery.getSkip() != null && commonQuery.getLimit() != null){
            query.skip(commonQuery.getSkip()).limit(commonQuery.getLimit());
        }
        return query;
    }

    /**
     * 需要过去的字段
     * @param fields
     * @return
     */
    public static Document getFields(List<String> fields){
        Document dbObjec = null;
        if(!CollectionUtils.isEmpty(fields)){
            dbObjec = new Document();
            for(String field : fields){
                if(!StringUtils.isEmpty(field)){
                    dbObjec.put(field, true);
                }
            }
        }
        return dbObjec;
    }

    public static Query getQuery(List<FieldFilter> fieldFilters){
        return getQuery(fieldFilters, null);
    }

    /**
     * 针对id做处理
     * 若value为集合，若集合元素为字符串，则处理成ObjectId，若值为ObjectId，则不处理，若值为
     * @param field
     * @param value
     * @return
     */
    public static Object getValue(String field, Object value){
        if(value == null){
            return null;
        }
        if(BaseEntity.ID.equals(field)){
            if(value instanceof Collection){
                if(((Collection) value).isEmpty()){
                    return value;
                }else{
                    List<ObjectId> ids = new ArrayList<>(((Collection) value).size());
                    ((Collection) value).forEach(v -> {
                        ObjectId objectId = getId(v);
                        if(objectId != null){
                            ids.add(objectId);
                        }
                    });
                    return ids;
                }
            }else if(value instanceof String){
                return getId(value);
            }else{
                throw new IllegalArgumentException("id is illegal");
            }
        }else{
            return value;
        }
    }

    /**
     * 转换内置的mongo字段
     * @param field
     * @return
     */
    public static String getField(String field){
        Assert.validateStringEmpty(field, "NO_FIELD", "field can not be empty");
        return INNER_FIELD_MAP.getOrDefault(field, field);
    }

    public static Query getQuery(List<FieldFilter> fieldFilters, List<String> fields){
        Query query;
        Document dbObjec = getFields(fields);
        Criteria criteria = new Criteria();
        if(!CollectionUtils.isEmpty(fieldFilters)){
            for(FieldFilter fieldFilter : fieldFilters){
                String field = getField(fieldFilter.getField());
                Object value = getValue(fieldFilter.getField(), fieldFilter.getValue());

                if(StringUtils.isEmpty(fieldFilter.getOp())){
                    fieldFilter.setOp(MongoOp.EQ.name());
                }
                String op = fieldFilter.getOp();
                if(Utils.equals(MongoOp.LT.name(), op)){
                    criteria.and(field).lt(value);
                }else if(Utils.equals(MongoOp.LTE.name(), op)){
                    criteria.and(field).lte(value);
                }else if(Utils.equals(MongoOp.GT.name(), op)){
                    criteria.and(field).gt(value);
                }else if(Utils.equals(MongoOp.GTE.name(), op)){
                    criteria.and(field).gte(value);
                }else if(Utils.equals(MongoOp.REGEX.name(), op)){
                    criteria.and(field).regex(Utils.nullToString(value));
                }else if(Utils.equals(MongoOp.IN.name(), op)){
                    criteria.and(field).in((Collection)value);
                }else if(Utils.equals(MongoOp.NE.name(), op)){
                    criteria.and(field).ne(value);
                }else if(Utils.equals(MongoOp.NIN.name(), op)){
                    criteria.and(field).nin((Collection)value);
                }else if(Utils.equals(MongoOp.EXISTS.name(), op)){
                    criteria.and(field).exists((Boolean) value);
                }else{
                    criteria.and(field).is(value);
                }
            }
        }
        if(dbObjec != null){
            query = new BasicQuery(criteria.getCriteriaObject(), dbObjec);
        }else{
            query = new Query(criteria);
        }
        return query;
    }

    public static Update getUpdate(List<FieldUpdate> updates){
        Update update = new Update();
        updates.forEach((temp) -> update.set(temp.getField(), temp.getValue()));
        return update;
    }

    public static Update getIncUpdate(List<FieldUpdate> updates){
        Update update = new Update();
        updates.forEach((temp) -> {
            if(temp.getValue() != null){
                update.inc(temp.getField(), NumberUtils.parseNumber(temp.getValue().toString(), Integer.class));
            }

        });
        return update;
    }
}
