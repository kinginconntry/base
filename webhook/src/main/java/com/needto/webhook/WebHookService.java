package com.needto.webhook;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.needto.common.entity.Filter;
import com.needto.common.entity.Query;
import com.needto.common.entity.Result;
import com.needto.common.utils.*;
import com.needto.dao.common.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.dao.common.FieldFilter;
import com.needto.dao.common.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * webhook服务
 */
@Service
public class WebHookService {

    private static final Logger LOG = LoggerFactory.getLogger(WebHookService.class);

    /**
     * 重试默认等待时间：毫秒
     */
    public static final int DEFAULT_WAIT_TIME = 60 * 1000;

    @Autowired
    private CommonDao mongoDao;

    public WebHook save(WebHook webHook){
        Assert.validateNull(webHook, "webhook can not be null");
        Assert.validateNull(webHook.getOwner(), "belongTo can not be null");
        Assert.validateStringEmpty(webHook.getEvent(), "event can not be empty");
        Assert.validateCondition(ValidateUtils.isUrl(webHook.getUrl()), "INVALID_URL", "");
        return this.mongoDao.save(webHook, WebHook.TABLE);
    }

    public List<WebHook> find(Query query, String owner){
        Assert.validateStringEmpty(owner);
        if(query == null){
            query = new Query();
        }
        List<Filter> filters = query.getFilters();

        if(filters == null){
            filters = new ArrayList<>();
        }
        filters.add(new Filter("owner", owner));
        return this.mongoDao.find(CommonQueryUtils.getQuery(query), WebHook.class, WebHook.TABLE);
    }

    public long remove(List<Filter> fieldFilters, String owner){
        Assert.validateStringEmpty(owner);
        if(fieldFilters == null){
            fieldFilters = new ArrayList<>();
        }
        fieldFilters.add(new Filter("owner", owner));
        return this.mongoDao.delete(CommonQueryUtils.getFilters(fieldFilters), WebHook.TABLE);
    }

    public long removeByIds(List<String> ids, String owner){
        if(CollectionUtils.isEmpty(ids)){
            return 0L;
        }
        return this.mongoDao.delete(Lists.newArrayList(
                new FieldFilter("id", Op.IN.name(), ids),
                new FieldFilter("owner", owner)
        ), WebHook.TABLE);
    }

    public List<WebHook> find(String owner, String event){
        Assert.validateStringEmpty(owner);
        Assert.validateStringEmpty(event);
        List<FieldFilter> fieldFilters = Lists.newArrayList(
                new FieldFilter("owner", owner),
                new FieldFilter("event", event)
        );
        return this.mongoDao.find(fieldFilters, WebHook.class, WebHook.TABLE);
    }

    /**
     * 异步任务：消费事件，发送webhook;
     * @param owner
     * @param event
     */
    @Async
    public void consume(String owner, String event, Object data, int retry){
        List<WebHook> webHooks = this.find(owner, event);
        if(!CollectionUtils.isEmpty(webHooks)){
            for(WebHook webHook : webHooks){
                sendData(webHook, data, retry);
            }
        }
    }

    /**
     * 默认的事件消费器，重试3次
     * @param owner
     * @param event
     * @param data
     */
    public void consume(String owner, String event, Object data){
        this.consume(owner, event, data, 3);
    }

    /**
     * 向服务端发送数据
     * @param webHook
     * @param data
     * @return
     */
    private boolean sendData(WebHook webHook, Object data){
        String url = webHook.getUrl();

        String jsonStr = JSON.toJSONString(data);
        if(webHook.encryValid()){
            CryptoUtil.Crypto crypto = webHook.getEncryTypeCrypto();
            jsonStr = crypto.encry(jsonStr, webHook.getEncryKey());
        }
        if(webHook.signValid()){
            url = SignUtils.signUrl(url, webHook.getSignKey());
        }
        Map<String, String> headers = webHook.getHeaders();
        HttpHeaders httpHeaders = null;
        if(!CollectionUtils.isEmpty(headers)){
            httpHeaders = new HttpHeaders();
            for(Map.Entry<String, String> entry : headers.entrySet()){
                httpHeaders.set(entry.getKey(), entry.getValue());
            }
        }
        ResponseEntity<Object> res = ApiRequest.request(url, HttpMethod.POST, jsonStr, httpHeaders, Object.class);
        int status = res.getStatusCode().value();

        if(status >= 200 && status < 300){
            LOG.debug("发送webhook数据：url {}, data {}, headers {}, 返回状态码 {}", url, jsonStr, headers.toString(), status);
            return true;
        }else{
            LOG.error("发送webhook数据：url {}, data {}, headers {}, 返回状态码 {}，发送失败！", url, jsonStr, headers.toString(), status);
            return false;
        }
    }

    private boolean sendData(WebHook webHook, Object data, int retry){
        Assert.validateLessThan(retry, 0);
        Result<Boolean> res = RetryUtils.run(retry, DEFAULT_WAIT_TIME, "", () -> sendData(webHook, data), flag -> flag);
        return res.isSuccess();
    }

}
