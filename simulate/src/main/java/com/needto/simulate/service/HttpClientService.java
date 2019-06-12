package com.needto.simulate.service;

import com.needto.http.entity.HttpClientBuilder;
import com.needto.http.entity.HttpRequestClient;
import com.needto.http.entity.Method;
import com.needto.simulate.data.SimulateResponseEvent;
import com.needto.simulate.entity.Request;
import com.needto.simulate.entity.Response;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ResponseBody;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class HttpClientService {

    @Autowired
    private ApplicationContext applicationContext;

    private HttpRequestClient httpRequestClient;

    @PostConstruct
    public void init(){
        HttpClientBuilder httpClientBuilder = new HttpClientBuilder();
        httpRequestClient = new HttpRequestClient(httpClientBuilder.build());
    }

    private String getBody(okhttp3.Response responseRes){
        if(responseRes.body() != null){
            ResponseBody responseBody = responseRes.body();
            try {
                return responseBody.string();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Response execute(Request request){
        Response response = new Response();
        response.setRequest(request);
        response.setRequestStartTime(System.currentTimeMillis());
        okhttp3.Response responseRes = httpRequestClient.requestResponseByMethod(request.getUrlInfo().getUrl(), Method.valueOf(request.getMethod()), null, request.getBody(), request.getHeaders());
        response.setRequestEndTime(System.currentTimeMillis());
        response.setStatuscode(responseRes.code());
        response.setBody(getBody(responseRes));
        return response;
    }

    public void asyncExecute(Request request) {
        Response response = new Response();
        response.setRequestStartTime(System.currentTimeMillis());
        response.setRequest(request);
        SimulateResponseEvent responseEvent = new SimulateResponseEvent(this, response);
        if(StringUtils.isEmpty(request.getMethod())){
            request.setMethod("GET");
        }
        httpRequestClient.requestByMethod(request.getUrlInfo().getUrl(), Method.valueOf(request.getMethod()), null, request.getBody(), request.getHeaders(), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                response.setStatuscode(Response.FAILTURE);
                response.setRequestEndTime(System.currentTimeMillis());
                applicationContext.publishEvent(responseEvent);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response res) {
                response.setStatuscode(res.code());
                response.setRequestEndTime(System.currentTimeMillis());
                response.setBody(getBody(res));
                applicationContext.publishEvent(responseEvent);
            }
        });
    }
}
