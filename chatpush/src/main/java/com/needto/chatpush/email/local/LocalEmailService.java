package com.needto.chatpush.email.local;

import com.google.common.collect.Lists;
import com.needto.chatpush.email.IEmailService;
import com.needto.chatpush.email.local.event.EmailAfterSendErrorEvent;
import com.needto.chatpush.email.local.event.EmailAfterSendSuccessEvent;
import com.needto.common.context.SpringEnv;
import com.needto.email.entity.EmailData;
import com.needto.email.utils.EmailUtil;
import com.needto.tool.entity.Dict;
import com.needto.tool.exception.BaseException;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import javax.mail.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static com.needto.email.utils.EmailUtil.createEmail;

/**
 * @author Administrator
 * 本地邮件服务
 */
public class LocalEmailService implements IEmailService<EmailData> {

    public static final String CODE = "LOCAL_EMAIL";

    private final static String DEFAULT_EMAIL_PREFIX = "email.local";

    /**
     * 用户名key
     */
    private final static String USERNAME_KEY = "username";

    /**
     * 用户密码key
     */
    private final static String PASSWORD_KEY = "passowrd";

    private Session session;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Environment environment;

    public Session getSession() {
        return session;
    }

    /**
     * 初始化
     */
    @PostConstruct
    public void init(){

        Map<String, Map<String, Object>> map = SpringEnv.getProperties((AbstractEnvironment) environment, DEFAULT_EMAIL_PREFIX);
        for(Map.Entry<String, Map<String, Object>> entry : map.entrySet()){
            String key = entry.getKey();
            Assert.validateStringEmpty(key);
            Assert.validateNull(entry.getValue());
            session = buildSession(entry.getValue());
            break;
        }
    }

    /**
     * 根据配置生成email的 session
     * @param map
     * @return
     */
    public Session buildSession(Map<String, Object> map){
        Properties properties = new Properties();
        properties.putAll(map);
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

    /**
     * 发送邮件
     * @param session
     * @param emailDatas
     * @param username
     * @param password
     */
    public boolean send(Session session, List<EmailData> emailDatas, String username, String password){
        EmailUtil.checkEmailData(emailDatas);
        Transport ts = null;
        try {
            ts = session.getTransport();
            if(StringUtils.isEmpty(username)){
                // 默认已经设置好了
                ts.connect();
            }else{
                ts.connect(session.getProperty("mail.host"), username, password);
            }

            for(EmailData emailData : emailDatas){
                Message message = createEmail(session, emailData);
                ts.sendMessage(message, message.getAllRecipients());
                applicationContext.publishEvent(new EmailAfterSendSuccessEvent(this, emailData, session));
            }
            ts.close();
            return true;
        } catch (BaseException e) {
            e.printStackTrace();
            applicationContext.publishEvent(new EmailAfterSendErrorEvent(this, session, e.getErrCode(), e.getErrMsg()));
            throw new LogicException("", "");
        } catch (MessagingException e) {
            e.printStackTrace();
            applicationContext.publishEvent(new EmailAfterSendErrorEvent(this, session, "", ""));
            throw new LogicException("", "");
        } finally {
            if(ts != null){
                try {
                    ts.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 发送多封邮件
     * @param emailDatas
     * @param username
     * @param password
     */
    public boolean send(List<EmailData> emailDatas, String username, String password){
        Session session = getSession();
        return send(session, emailDatas, username, password);
    }

    /**
     * 发送单封邮件
     * @param emailData
     * @param username
     * @param password
     * @return
     */
    public boolean send(EmailData emailData, String username, String password){
        Session session = getSession();
        return send(session, Lists.newArrayList(emailData), username, password);
    }

    @Override
    public boolean send(EmailData emailData, Dict config) {

        String username = "";
        String password = "";
        if(config != null){
            username = config.getValue(USERNAME_KEY);
            password = config.getValue(PASSWORD_KEY);
        }
        Session session = getSession();
        return send(session, Lists.newArrayList(emailData), username, password);
    }

    @Override
    public boolean send(List<EmailData> emailDatas, Dict config){
        String username = "";
        String password = "";
        if(config != null){
            username = config.getValue(USERNAME_KEY);
            password = config.getValue(PASSWORD_KEY);
        }
        Session session = getSession();
        return send(session, emailDatas, username, password);
    }

    @Override
    public String getCode() {
        return CODE;
    }
}
