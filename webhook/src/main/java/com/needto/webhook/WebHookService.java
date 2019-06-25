package com.needto.webhook;

import com.alibaba.fastjson.JSON;
import com.needto.common.entity.Query;
import com.needto.dao.inter.CommonDao;
import com.needto.dao.common.CommonQueryUtils;
import com.needto.http.entity.HttpHeader;
import com.needto.http.utils.ApiRequest;
import com.needto.tool.entity.Filter;
import com.needto.tool.entity.Result;
import com.needto.tool.utils.*;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
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

    public <T extends WebHook> T save(T webHook){
        Assert.validateNull(webHook, "webhook can not be null");
        Assert.validateStringEmpty(webHook.getEvent(), "event can not be empty");
        Assert.validateCondition(ValidateUtils.isUrl(webHook.getUrl()), "INVALID_URL", "");
        return this.mongoDao.save(webHook, WebHook.TABLE);
    }

    public <T extends WebHook> List<T> find(Query query, Class<T> obj){
        return this.mongoDao.find(CommonQueryUtils.getQuery(query), obj, WebHook.TABLE);
    }

    public long delete(List<Filter> fieldFilters){
        return this.mongoDao.delete(CommonQueryUtils.getFilters(fieldFilters), WebHook.TABLE);
    }

    public <T extends WebHook> List<T> find(List<Filter> filters, Class<T> obj){
        return this.mongoDao.find(CommonQueryUtils.getFilters(filters), obj, WebHook.TABLE);
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
        HttpHeader httpHeaders = null;
        if(!CollectionUtils.isEmpty(headers)){
            httpHeaders = new HttpHeader();
            for(Map.Entry<String, String> entry : headers.entrySet()){
                httpHeaders.setHeader(entry.getKey(), entry.getValue());
            }
        }
        String finalUrl = url;
        String finalJsonStr = jsonStr;
        ApiRequest.post(url, null, jsonStr, httpHeaders, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LOG.debug("发送webhook数据：url {}, data {}, headers {}，发送失败！", finalUrl, finalJsonStr, headers.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LOG.error("发送webhook数据：url {}, data {}, headers {}", finalUrl, finalJsonStr, headers.toString());
            }
        });
        return true;
    }

    private boolean sendData(WebHook webHook, Object data, int retry){
        Assert.validateLessThan(retry, 0);
        Result<Boolean> res = RetryUtils.run(retry, DEFAULT_WAIT_TIME, "", () -> sendData(webHook, data), flag -> flag);
        return res.isSuccess();
    }

    private boolean sendData(List<WebHook> webHooks, Object data, int retry){
        if(CollectionUtils.isEmpty(webHooks)){
            return true;
        }
        for(WebHook webHook : webHooks){
            sendData(webHook, data, retry);
        }
        return true;
    }

}
