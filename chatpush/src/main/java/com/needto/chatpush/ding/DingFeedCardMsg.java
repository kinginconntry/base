package com.needto.chatpush.ding;


import java.util.List;

/**
 * @author Administrator
 */
public class DingFeedCardMsg extends DingMsg {

    public static class Link {
        /**
         * 单条信息文本
         */
        public String title;

        /**
         * 点击单条信息到跳转链接
         */
        public String messageURL;

        /**
         * 单条信息后面图片的URL
         */
        public String picURL;
    }
    public static class FeedCard {
        public List<Link> links;
    }

    public FeedCard feedCard;



    public DingFeedCardMsg() {
        super("feedCard");
        this.feedCard = new FeedCard();
    }


    public FeedCard getFeedCard() {
        return feedCard;
    }

    public void setFeedCard(FeedCard feedCard) {
        this.feedCard = feedCard;
    }
}
