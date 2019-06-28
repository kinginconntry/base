package com.needto.chatpush.ding.entity;

import java.util.List;

public class At {
    /**
     * 被@人的手机号(在content里添加@人的手机号)
     */
    public List<String> atMobiles;

    /**
     * {@literal @所有人时：true，否则为：false}
     */
    public boolean isAtAll;
}
