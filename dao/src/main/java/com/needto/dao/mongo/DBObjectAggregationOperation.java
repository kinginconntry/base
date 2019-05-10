package com.needto.dao.mongo;


import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

/**
 * @author Administrator
 * dbObject聚合操作类
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
