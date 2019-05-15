package com.needto.chatpush.email.util;

import org.apache.commons.lang.StringUtils;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.List;

/**
 * @author Administrator
 */
public class EmailUtil {

    /**
     * 获取邮箱地址
     * @param to
     * @return
     * @throws AddressException
     */
    public static InternetAddress[] getAddress(List<String> to) throws AddressException {
        if(to == null){
            return null;
        }
        InternetAddress[] toAddr = new InternetAddress[to.size()];
        for(int i = 0, len = to.size(); i < len; i++){
            String temp = to.get(i);
            if(!StringUtils.isEmpty(temp)){
                toAddr[i] = new InternetAddress(temp);
            }
        }
        return toAddr;
    }
}
