package com.needto.dao.mongo;


import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * @author Administrator
 * mongo聚合操作类, 使用DBObject接口实例来表达聚合内容
 */
public class DBObjectAggregationOperation implements AggregationOperation {

    private DBObject operation;

    public DBObjectAggregationOperation(DBObject operation) {
        this.operation = operation;
    }

    @Override
    public Document toDocument(AggregationOperationContext aggregationOperationContext) {
        return aggregationOperationContext.getMappedObject(new Document(this.operation.toMap()));
    }
}
