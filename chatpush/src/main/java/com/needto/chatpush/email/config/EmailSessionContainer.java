package com.needto.chatpush.email.config;

import com.needto.common.utils.Env;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Administrator
 * 邮件session容器
 */
@Component
public class EmailSessionContainer {

    private final static String DEFAULT_EMAIL_PREFIX = "email.server.";

    private final static String USERNAME_KEY = "username";
    private final static String PASSWORD_KEY = "passowrd";

    private static final Map<String, Session> SESSION_MAP = new HashMap<>();

    @Autowired
    private Environment environment;

    @PostConstruct
    public void init(){
        Map<String, Map<String, Object>> map = Env.getProperties((AbstractEnvironment) environment, DEFAULT_EMAIL_PREFIX);
        for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
            String key = entry.getKey();
            Assert.validateStringEmpty(key);
            Session session = getSession(entry.getValue());
            set(key, session);
        }
    }

    public Properties getProperties(Map<String, Object> map){
        Properties properties = new Properties();
        properties.putAll(map);
        return properties;
    }

    public Session getSession(Map<String, Object> map){

        Assert.validateCondition(CollectionUtils.isEmpty(map));
        Properties properties = getProperties(map);
        Authenticator authenticator = null;
        if(map.containsKey(USERNAME_KEY) || map.containsKey(PASSWORD_KEY)){
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(Utils.nullToString(map.get(USERNAME_KEY)), Utils.nullToString(map.get(PASSWORD_KEY)));
                }
            };
        }
        return Session.getInstance(properties, authenticator);

    }

    public Session get(String sessionId){
        Assert.validateStringEmpty(sessionId);
        return SESSION_MAP.get(sessionId);
    }
    public void set(String sessionId, Session session){
        Assert.validateStringEmpty(sessionId);
        Assert.validateNull(session);
        SESSION_MAP.put(sessionId, session);
    }
}
