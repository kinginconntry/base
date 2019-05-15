package com.needto.chatpush.email;

import com.needto.chatpush.email.config.EmailSessionContainer;
import com.needto.chatpush.email.entity.EmailData;
import com.needto.chatpush.email.entity.IBodyBuilder;
import com.needto.chatpush.email.event.EmailAfterSendErrorEvent;
import com.needto.chatpush.email.event.EmailAfterSendSuccessEvent;
import com.needto.chatpush.email.util.EmailUtil;
import com.needto.common.exception.BaseException;
import com.needto.common.exception.LogicException;
import com.needto.common.utils.Assert;
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
     * 创建邮件
     * @param session
     * @param emailData
     * @return
     */
    private MimeMessage createEmail(Session session, EmailData emailData) {
        try {
            InternetAddress[] to = EmailUtil.getAddress(emailData.getTo());

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailData.getFrom()));
            message.setRecipients(Message.RecipientType.TO, to);

            InternetAddress[] cc = EmailUtil.getAddress(emailData.getCc());
            if(cc != null && cc.length != 0){
                message.setRecipients(Message.RecipientType.CC, cc);
            }
            InternetAddress[] bcc = EmailUtil.getAddress(emailData.getBcc());
            if(bcc != null && bcc.length != 0){
                message.setRecipients(Message.RecipientType.BCC, bcc);
            }
            message.setSubject(emailData.getSubject());
            if(!CollectionUtils.isEmpty(emailData.getImages())){
                MimeBodyPart content = emailData.getContent().builder();
                MimeMultipart mp = new MimeMultipart();
                mp.addBodyPart(content);
                for(IBodyBuilder image : emailData.getImages()){
                    mp.addBodyPart(image.builder());
                }
                mp.setSubType("related");
                if(!CollectionUtils.isEmpty(emailData.getAttachs())){
                    MimeMultipart mpattach = new MimeMultipart();
                    for(IBodyBuilder attach : emailData.getAttachs()){
                        mpattach.addBodyPart(attach.builder());
                    }
                    MimeBodyPart attachContent = new MimeBodyPart();
                    attachContent.setContent(mp);
                    mpattach.addBodyPart(attachContent);
                    mpattach.setSubType("mixed");
                    mpattach.setSubType("related");
                    message.setContent(mpattach);
                }else{
                    message.setContent(mp);
                }
            }else{
                if(!CollectionUtils.isEmpty(emailData.getAttachs())){
                    MimeBodyPart content = emailData.getContent().builder();
                    MimeMultipart mp = new MimeMultipart();
                    mp.addBodyPart(content);
                    for(IBodyBuilder attach : emailData.getAttachs()){
                        mp.addBodyPart(attach.builder());
                    }
                    mp.setSubType("mixed");
                    message.setContent(mp);
                }else{
                    message.setContent(emailData.getContent().getContent(), emailData.getContent().getType());
                }

            }
            if(!CollectionUtils.isEmpty(emailData.getHeaders())){
                for(Map.Entry<String, String> header : emailData.getHeaders().entrySet()){
                    message.setHeader(header.getKey(), header.getValue());
                }
            }
            message.saveChanges();
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new LogicException("", "");
        }
    }

    /**
     * 检查邮件数据
     * @param emailDatas
     */
    private void checkEmailData(List<EmailData> emailDatas){
        Assert.validateCondition(CollectionUtils.isEmpty(emailDatas));
        for(EmailData emailData : emailDatas){
            Assert.validateNull(emailData);
            Assert.validateStringEmpty(emailData.getFrom());
            Assert.validateStringEmpty(emailData.getSubject());
            Assert.validateCondition(CollectionUtils.isEmpty(emailData.getTo()));
        }
    }

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
