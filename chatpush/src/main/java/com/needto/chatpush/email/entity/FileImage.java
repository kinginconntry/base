package com.needto.chatpush.email.entity;

import com.needto.common.exception.LogicException;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;

/**
 * @author Administrator
 * 本地图片数据
 */
public class FileImage implements IBodyBuilder {
    /**
     * 本地图片路径
     */
    private String path;
    /**
     *
     */
    private String contentId;

    public FileImage(String path, String contentId) {
        Assert.validateStringEmpty(path, "EMPTY_PATH", "");
        Assert.validateStringEmpty(contentId, "EMPTY_CONTENTID", "");
        this.path = path;
        this.contentId = contentId;
    }

    public String getPath() {
        return path;
    }

    public String getContentId() {
        return contentId;
    }

    /**
     * 默认使用本地文件
     * @return
     */
    @Override
    public MimeBodyPart builder() {

        try {
            MimeBodyPart image = new MimeBodyPart();
            image.setDataHandler(new DataHandler(new FileDataSource(this.path)));
            image.setContentID(this.contentId);
            return image;
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new LogicException("IMAGE_BUILDER_FAILTURE", "");
        }

    }
}
