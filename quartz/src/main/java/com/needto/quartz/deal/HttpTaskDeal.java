package com.needto.quartz.deal;

import com.alibaba.fastjson.JSON;
import com.needto.common.utils.*;
import com.needto.quartz.entity.TaskData;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * http 请求任务
 */
@Component
@TaskDealRegister(code = "http", name="http任务", desc="发送http请求到相应服务器")
public class HttpTaskDeal implements TaskDeal {

    private static final String URL = "url";
    private static final String METHOD = "method";
    private static final String BODY = "body";
    private static final String HEADERS = "headers";
    private static final String SIGN = "signmode";
    private static final String SIGNKEY = "signkey";

    private static final String BODY_CRYPTO = "bodyCryptoMode";
    private static final String BODY_CRYPTO_KEY = "bodyCryptoKey";

    @Override
    public void deal(TaskData data) {

        String url = data.getData().getValue(URL);
        String method = data.getData().getValue(METHOD);
        Object body = data.getData().getValue(BODY);
        Map<String, Object> headers = data.getData().getValue(HEADERS);
        String signMode = data.getData().getValue(SIGN);
        String signkey = data.getData().getValue(SIGNKEY);
        if(!StringUtils.isEmpty(signMode) && !StringUtils.isEmpty(signkey)){
            url = SignUtils.signUrl(url, signkey, signMode);
        }

        String bodyCryptoMode = data.getData().getValue(BODY_CRYPTO);
        String bodyCryptoKey = data.getData().getValue(BODY_CRYPTO_KEY);
        if(!StringUtils.isEmpty(bodyCryptoMode) && !StringUtils.isEmpty(bodyCryptoKey) && body != null){
            body = CryptoUtil.Crypto.valueOf(bodyCryptoMode).encry(JSON.toJSONString(body), bodyCryptoKey);
        }
        HttpHeaders httpHeaders = null;
        if(!CollectionUtils.isEmpty(headers)){
            httpHeaders = new HttpHeaders();
            for(Map.Entry<String, Object> entry : headers.entrySet()){
                if(!StringUtils.isEmpty(entry.getKey())){
                    httpHeaders.set(entry.getKey(), Utils.nullToString(entry.getValue()));
                }
            }
        }
        Assert.validateCondition(ValidateUtils.isUrl(url));
        HttpMethod httpMethod;
        if(!StringUtils.isEmpty(method)){
            httpMethod = HttpMethod.valueOf(method);
        }else{
            if(body != null){
                httpMethod = HttpMethod.POST;
            }else{
                httpMethod = HttpMethod.GET;
            }
        }
        ApiRequest.defaultSourceRequest(new RestTemplate(), url, body, httpMethod, httpHeaders, null, Object.class);
    }
}
