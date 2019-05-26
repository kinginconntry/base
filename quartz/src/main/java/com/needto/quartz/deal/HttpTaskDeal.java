package com.needto.quartz.deal;

import com.alibaba.fastjson.JSON;
import com.needto.http.entity.HttpHeader;
import com.needto.http.entity.Method;
import com.needto.http.utils.ApiRequest;
import com.needto.quartz.entity.TaskData;
import com.needto.tool.utils.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Administrator
 * http 请求任务
 */
@Component
public class HttpTaskDeal implements TaskDeal {

    public static final String DEAL_CODE = "HTTP";

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
        HttpHeader httpHeaders = null;
        if(!CollectionUtils.isEmpty(headers)){
            httpHeaders = new HttpHeader();
            for(Map.Entry<String, Object> entry : headers.entrySet()){
                if(!StringUtils.isEmpty(entry.getKey())){
                    httpHeaders.setHeader(entry.getKey(), Utils.nullToString(entry.getValue()));
                }
            }
        }
        Assert.validateCondition(ValidateUtils.isUrl(url));
        Method httpMethod;
        if(!StringUtils.isEmpty(method)){
            httpMethod = Method.valueOf(method);

        }else{
            if(body != null){
                httpMethod = Method.POST;
            }else{
                httpMethod = Method.GET;
            }
        }
        ApiRequest.request(url, httpMethod, null, body, httpHeaders, null);
    }

    @Override
    public String getCode() {
        return DEAL_CODE;
    }
}
