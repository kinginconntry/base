package com.needto.es;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Component;


@Component
public class EsDao{

    @Autowired
    private ElasticsearchTemplate template;

}
