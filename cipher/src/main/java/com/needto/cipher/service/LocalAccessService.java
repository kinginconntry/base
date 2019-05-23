package com.needto.cipher.service;

import com.needto.common.utils.CryptoUtil;
import com.needto.common.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 */
@Service
@ConditionalOnProperty(name = "localapiaccess", havingValue = "true")
public class LocalAccessService {

    private static final Map<String, Object> DEFAULT = new HashMap<>();

    @Autowired
    private Environment environment;

    private String accessId;

    private String secret;

    private CryptoUtil.Crypto crypto;

    @PostConstruct
    public void init(){
        this.accessId = environment.getProperty("localapiaccess.accessId", "root");
        this.secret = environment.getProperty("localapiaccess.secret", "root");
        this.crypto = CryptoUtil.Crypto.valueOf(environment.getProperty("localapiaccess.crypto", "MD5"));
        DEFAULT.put("accessId", accessId);
    }


    public boolean validate(String accessId, Map<String, Object> attrs, String sign){
        if(StringUtils.isEmpty(accessId) || StringUtils.isEmpty(sign)){
            return false;
        }
        if(attrs == null){
            attrs = DEFAULT;
        }else{
            attrs.put("accessId", accessId);
        }
        String sg = SignUtils.getParamSign(attrs, this.secret, this.crypto.key);
        return sign.equals(sg);
    }

}
