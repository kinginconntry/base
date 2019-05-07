package com.needto.dao.common;

import com.needto.common.entity.Query;

public class CommonQueryUtils {

    public static CommonQuery getQuery(Query query){

        CommonQuery commonQuery = new CommonQuery();
        if(query == null){
            return commonQuery;
        }
        commonQuery.limit = query.getLimit();
        commonQuery.skip = query.getSkip();
        commonQuery.orders = query.orders;
        commonQuery.fields = query.fields;
        commonQuery.filters = query.filters;
        return commonQuery;
    }


}
