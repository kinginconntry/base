package com.needto.httprequest.service;

import com.alibaba.fastjson.JSON;
import com.needto.common.context.MessageConvert;
import com.needto.common.entity.Dict;
import com.needto.common.utils.Utils;
import com.needto.httprequest.factory.HttpsClientRequestFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Administrator
 * TODO 1 增加文件上传功能 2 增加大文件支持 3增加超时等支持 4 重定向支持
 */
public class ApiRequest {

    private final static Logger LOG = LoggerFactory.getLogger(ApiRequest.class);
    /**
     *
     */
    public static final String CON_REQUEST_TIMEOUT = "connectionRequestTimeout";

    public static final String CON_TIMEOUT = "connectionTimeout";

    public static final String READ_TIMEOUT = "readTimeout";

    public static final String REDIRECT_STRATEGY = "redirectStrategy";

    public static final String COOKIE_MANAGER = "cookieStrategy";

    public static final String BUFFER_REQUEST_BODY = "bufferRequestBody";

    public static final String CHARSET = "charset";

    public static final String JSON_DATA = "json";

    /**
     * 默认的api请求处理：json传输，设置超时，重定向，cookie策略
     * FIXME 需要进一步优化
     * @param client
     * @param url
     * @param body
     * @param method
     * @param headers
     * @param extra
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> defaultSourceRequest(RestTemplate client, String url, Object body, HttpMethod method, HttpHeaders headers, Dict extra, Class<T> tClass){
        if(extra == null){
            extra = new Dict();
        }
        ClientHttpRequestFactory factory;
        if(url.startsWith("https")){
            factory = new HttpsClientRequestFactory();
        }else{
            HttpComponentsClientHttpRequestFactory temp = new HttpComponentsClientHttpRequestFactory();
            temp.setConnectionRequestTimeout(extra.getValue(CON_REQUEST_TIMEOUT, 2000));
            temp.setConnectTimeout(extra.getValue(CON_TIMEOUT, 10000));
            temp.setReadTimeout(extra.getValue(READ_TIMEOUT, 7200000));
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            if(extra.getValue(REDIRECT_STRATEGY, true)){
                httpClientBuilder.setRedirectStrategy(new LaxRedirectStrategy());
            }else{
                httpClientBuilder.disableRedirectHandling();
            }
            if(!extra.getValue(COOKIE_MANAGER, true)){
                httpClientBuilder.disableCookieManagement();
            }
            temp.setBufferRequestBody(extra.getValue(BUFFER_REQUEST_BODY, false));
            temp.setHttpClient(httpClientBuilder.build());
            factory = temp;
        }

        client.setRequestFactory(factory);
        Charset charset = extra.getValue(CHARSET, Charset.forName("UTF-8"));

        client.getMessageConverters().clear();
        client.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        client.getMessageConverters().add(new StringHttpMessageConverter());
        client.getMessageConverters().add(new ResourceHttpMessageConverter(false));
        client.getMessageConverters().add(new SourceHttpMessageConverter<>());
        client.getMessageConverters().add(new AllEncompassingFormHttpMessageConverter());
        client.getMessageConverters().add(new StringHttpMessageConverter(charset));
        if(extra.getValue(JSON_DATA, true)){
            client.getMessageConverters().add(MessageConvert.getHttpMessageConverters());
        }else{
            client.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());
        }

        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        LOG.debug("{}: url {}, body {}, headers {}, extra {}", method.name(), url, JSON.toJSONString(body), JSON.toJSONString(headers), JSON.toJSONString(extra));
        return client.exchange(url, method, entity, tClass);
    }

    /**
     * 最原始的请求，可自定义请求工厂
     * @param client
     * @param url
     * @param body
     * @param method
     * @param headers
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> ResponseEntity<T> sourceRequest(RestTemplate client, String url, Object body, HttpMethod method, HttpHeaders headers, Class<T> tClass) {
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);
        LOG.debug("{}: url {}, body {}, headers {}, extra {}", method.name(), url, JSON.toJSONString(body), JSON.toJSONString(headers));
        return client.exchange(url, method, entity, tClass);
    }

    // 泛型
    public static <T> ResponseEntity<T> request(String url, HttpMethod method, Object body, HttpHeaders headers, Dict extra, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, extra, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, Object body, HttpHeaders headers, Dict extra, Class<T> tClass){
        return request(url, method, body, headers, extra, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, Object body, HttpHeaders headers, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, null, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, Object body, HttpHeaders headers, Class<T> tClass) {
        return request(url, method, body, headers, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, Object body, Dict extra, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, extra, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, Object body, Dict extra, Class<T> tClass){
        return request(url, method, body, extra, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, HttpHeaders headers, Dict extra, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, extra, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, HttpHeaders headers, Dict extra, Class<T> tClass){
        return request(url, method, headers, extra, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, Object body, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, null, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, Object body, Class<T> tClass){
        return request(url, method, body, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, HttpHeaders headers, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, null, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, HttpHeaders headers, Class<T> tClass){
        return request(url, method, headers, tClass).getBody();
    }

    public static <T> ResponseEntity<T> request(String url, HttpMethod method, Class<T> tClass){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, null, null, tClass);
    }

    public static <T> T requestBody(String url, HttpMethod method, Class<T> tClass){
        return request(url, method, tClass).getBody();
    }

    // Dict
    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, Object body, HttpHeaders headers, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, extra, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, Object body, HttpHeaders headers){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, null, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, Object body, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, extra, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, HttpHeaders headers, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, extra, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, Object body){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, null, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method, HttpHeaders headers){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, null, Dict.class);
    }

    public static ResponseEntity<Dict> requestDict(String url, HttpMethod method){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, null, null, Dict.class);
    }

    // String
    public static ResponseEntity<String> requestString(String url, HttpMethod method, Object body, HttpHeaders headers, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, extra, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method, Object body, HttpHeaders headers){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, headers, null, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method, Object body, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, extra, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method, HttpHeaders headers, Dict extra){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, extra, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method, Object body){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, body, method, null, null, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method, HttpHeaders headers){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, headers, null, String.class);
    }

    public static ResponseEntity<String> requestString(String url, HttpMethod method){
        RestTemplate client = new RestTemplate();
        return defaultSourceRequest(client, url, null, method, null, null, String.class);
    }

    private RestTemplate restTemplate;

    public ApiRequest(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    // post

    public <T> ResponseEntity<T> post(String url, Object body, HttpHeaders headers, Class<T> tClass){
        return sourceRequest(restTemplate, url, body, HttpMethod.POST, headers, tClass);
    }

    public <T> ResponseEntity<T> post(String url, Object body, Class<T> tClass){
        return sourceRequest(restTemplate, url, body, HttpMethod.POST, null, tClass);
    }

    public <T> ResponseEntity<T> post(String url, HttpHeaders headers, Class<T> tClass){
        return sourceRequest(restTemplate, url, null, HttpMethod.POST, headers, tClass);
    }

    public <T> ResponseEntity<T> post(String url, Class<T> tClass){
        return sourceRequest(restTemplate, url, null, HttpMethod.POST, null, tClass);
    }

    // get

    public <T> ResponseEntity<T> get(String url, Map<String, String> params, HttpHeaders headers, Class<T> tClass){
        url = Utils.setUrlParam(url, params);
        return sourceRequest(restTemplate, url, null, HttpMethod.GET, headers, tClass);
    }


    public <T> ResponseEntity<T> get(String url, Map<String, String> params, Class<T> tClass){
        url = Utils.setUrlParam(url, params);
        return sourceRequest(restTemplate, url, null, HttpMethod.GET, null, tClass);
    }

    public <T> ResponseEntity<T> get(String url, HttpHeaders headers, Class<T> tClass){
        return sourceRequest(restTemplate, url, null, HttpMethod.GET, headers, tClass);
    }

    public <T> ResponseEntity<T> get(String url, Class<T> tClass){
        return sourceRequest(restTemplate, url, null, HttpMethod.GET, null, tClass);
    }

}
