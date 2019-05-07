package com.needto.common.services.dynamictoken;


import com.needto.common.entity.Result;
import com.needto.common.utils.ApiRequest;
import com.needto.common.utils.Crypto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @author Administrator
 * 内部服务动态签名调用
 */
@Service
public class DynamicSignService {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicSignService.class);

    @Value("${servicemanager.protocol}")
    private String protocol;

    @Value("${servicemanager.host}")
    private String host;

    @Value("${servicemanager.port}")
    private String port;

    @Value("${servicemanager.username}")
    private String username;

    @Value("${servicemanager.password}")
    private String password;

    @PostConstruct
    public void init(){
        this.updateSign();
    }

    private final List<String> signs = new ArrayList<>(2);

    public void updateSign(){
        // 以表单的方式提交
        String url = String.format("%s://%s:%s/_app/getSign", protocol, host, port);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Basic "+ Crypto.BASE64.encry(String.format("%s:%s", username, password), ""));
        Result result = ApiRequest.requestBody(url, HttpMethod.GET, httpHeaders, Result.class);
        if(result.isSuccess()){
            String sign = (String) result.getData();
            if(signs.contains(sign)){
               return;
            }
            if(signs.size() == 2){
                signs.remove(0);
            }
            signs.add(sign);
        }
    }

    public boolean isValid(String sign){
        if(signs.size() == 0){
            updateSign();
            if(signs.size() == 0){
                return false;
            }
        }
        if(signs.contains(sign)){
            return true;
        }else{
            return false;
        }
    }

    public List<String> getSigns(){
        if(signs.size() == 0){
            updateSign();
        }
        return signs;
    }

}
