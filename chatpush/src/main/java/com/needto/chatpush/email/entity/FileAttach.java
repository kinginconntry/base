package com.needto.chatpush.email.entity;


import com.needto.common.exception.LogicException;
import com.needto.common.utils.Assert;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;

/**
 * @author Administrator
 * 本地附件
 */
public class FileAttach implements IBodyBuilder {
    /**
     * 本地附件路径
     */
    private String path;

    public FileAttach(String path) {
        Assert.validateStringEmpty(path);
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    @Override
    public MimeBodyPart builder() {
        try {
            MimeBodyPart attach = new MimeBodyPart();
            DataHandler dh2 = new DataHandler(new FileDataSource(this.path));
            attach.setDataHandler(dh2);
            attach.setFileName(MimeUtility.encodeText(dh2.getName()));
            return attach;
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LogicException("ATTACH_BUILDER_FAILTURE", "");
        }

    }
}
