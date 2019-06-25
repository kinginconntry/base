package com.needto.chatpush.email;

import com.needto.chatpush.email.config.EmailSessionContainer;
import com.needto.chatpush.email.entity.EmailData;
import com.needto.chatpush.email.entity.IBodyBuilder;
import com.needto.chatpush.email.event.EmailAfterSendErrorEvent;
import com.needto.chatpush.email.event.EmailAfterSendSuccessEvent;
import com.needto.chatpush.email.util.EmailUtil;
import com.needto.tool.exception.BaseException;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * 邮件服务
 */
@Service
public class EmailService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private EmailSessionContainer emailSessionContainer;

    /**
     * 发送邮件
     * @param session
     * @param emailDatas
     * @param username
     * @param password
     */
    public boolean send(Session session, List<EmailData> emailDatas, String username, String password){
        Assert.validateNull(session, "NO_SESSION", "");
        Assert.validateStringEmpty(username, "NO_USERNAME", "");
        Assert.validateStringEmpty(password, "NO_PASSWORD", "");
        checkEmailData(emailDatas);
        Transport ts = null;
        try {
            ts = session.getTransport();
            ts.connect(session.getProperty("mail.host"), username, password);
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
     * 发送邮件
     * @param sessionId
     * @param emailDatas
     * @param username
     * @param password
     */
    public boolean send(String sessionId, List<EmailData> emailDatas, String username, String password){
        Session session = emailSessionContainer.get(sessionId);
        return send(session, emailDatas, username, password);
    }
}
