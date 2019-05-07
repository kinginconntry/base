package com.needto.common.utils;


import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author Administrator
 */

public enum Crypto {

    /**
     * md5
     */
    MD5("1"){
        @Override
        public String encry(String text, String key) {
            String temp = Utils.nullToString(text) + Utils.nullToString(key);
            return DigestUtils.md5DigestAsHex(temp.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public String decode(String text, String key) {
            return text;
        }
    },
    /**
     * rsa
     */
    RSA("2"){
        @Override
        public String encry(String text, String key) {
            return text;
        }

        @Override
        public String decode(String text, String key) {
            return text;
        }
    },
    /**
     * base64
     */
    BASE64("3"){
        @Override
        public String encry(String text, String key) {
            String temp = Utils.nullToString(text) + Utils.nullToString(key);
            return new String(Base64.getEncoder().encode(temp.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        }

        @Override
        public String decode(String text, String key) {
            if(text == null){
                return null;
            }
            return new String(Base64.getDecoder().decode(text.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        }
    };
    public String key;
    Crypto(String key){
        this.key = key;
    }
    public static boolean contain(String key){
        for(Crypto encry : Crypto.values()){
            if(encry.key.equals(key)){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据key获取加密对象
     * @param key
     * @return
     */
    public static Crypto getCrypto(String key){
        for(Crypto encry : Crypto.values()){
            if(encry.key.equals(key)){
                return encry;
            }
        }
        return null;
    }

    /**
     * 全部使用utf-8，若不相同需要更改编码后再进行编码
     * @param text
     * @param key
     * @return
     */
    abstract public String encry(String text, String key);
    /**
     * 全部使用utf-8，若不相同需要更改编码后再进行解码
     * @param text
     * @param key
     * @return
     */
    abstract public String decode(String text, String key);
}
