package com.needto.notice.entity;


/**
 * @author Administrator
 * 链接消息
 */
public class LinkMsg extends AbstractNoticeMsg {

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subHeading;

    /**
     * 内容
     */
    private String content;

    /**
     * 详情链接
     */
    private String detailLink;

    public LinkMsg(){
        super();
        this.type = "link";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetailLink() {
        return detailLink;
    }

    public void setDetailLink(String detailLink) {
        this.detailLink = detailLink;
    }

    public String getSubHeading() {
        return subHeading;
    }

    public void setSubHeading(String subHeading) {
        this.subHeading = subHeading;
    }

}
