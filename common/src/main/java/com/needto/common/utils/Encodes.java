/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.needto.common.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * 封装各种格式的编码解码工具类.
 *
 * 1.Commons-Codec的 hex/base64 编码
 * 2.Commons-Lang的xml/html escape
 * 3.JDK提供的URLEncoder
 *
 * @author calvin
 */
public class Encodes {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";
    private static Pattern pattern = Pattern.compile("\\\\u([0-9A-F]{4})", Pattern.DOTALL);

    public static byte[] intToBytes(int value) {
        byte[] bs = new byte[4];
        for (int i = 0; i < bs.length; i++) {
            bs[bs.length - 1 - i] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return bs;
    }

    public static int bytesToInt(byte[] bs) {
        int value = 0;
        for (int i = 0; i < bs.length; i++) {
            value |= (((int) (bs[bs.length - 1 - i] & 0xFF)) << 8 * i);
        }
        return value;
    }

    public static byte[] longToBytes(long value) {
        byte[] bs = new byte[8];
        for (int i = 0; i < bs.length; i++) {
            bs[bs.length - 1 - i] = (byte) ((value >> (8 * i)) & 0xFF);
        }
        return bs;
    }

    public static long bytesToLong(byte[] bs) {
        long value = 0;
        for (int i = 0; i < bs.length; i++) {
            value |= (((long) (bs[bs.length - 1 - i] & 0xFF)) << 8 * i);
        }
        return value;
    }


    /**
     * Hex编码.
     */
    public static String encodeHex(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
     * Hex解码.
     */
    public static byte[] decodeHex(String input) {
        if(StringUtils.isBlank(input)){
            return null;
        }
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            return null;
        }
    }

    /**
     * Base64编码.返回的字符为utf-8编码的字符串
     */
    public static String encodeBase64(byte[] input) {
        return Base64.encodeBase64String(input);
    }

    /**
     * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).返回的字符为utf-8编码的字符串
     */
    public static String encodeUrlSafeBase64(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
     * Base64编码, 根据参数确定是否URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548)
     */
    public static byte[] encodeBase64(byte[] input, boolean urlSafe) {
        return urlSafe ? Base64.encodeBase64URLSafe(input) : Base64.encodeBase64(input);
    }

    /**
     * Base64解码.
     */
    public static byte[] decodeBase64(String input) {
        return Base64.decodeBase64(input);
    }

    /**
     * Base64解码.
     * @param input Base64编码字节
     * @param encoding 解码字符串的编码，默认utf-8
     * @return 解码后字符串
     */
    public static String decodeBase64(String input, String encoding) {
        try {
            return new String(Base64.decodeBase64(input), StringUtils.defaultIfEmpty(encoding, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Html 转码.
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml(html);
    }

    /**
     * Html 解码.
     */
    public static String unescapeHtml(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml(htmlEscaped);
    }

    /**
     * Xml 转码.
     */
    public static String escapeXml(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    /**
     * Xml 解码.
     */
    public static String unescapeXml(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }

    /**
     * URL 编码, Encode默认为UTF-8.
     */
    public static String urlEncode(String part) {
        if (StringUtils.isBlank(part)) {
            return "";
        }
        try {
            return URLEncoder.encode(part, DEFAULT_URL_ENCODING);
        } catch (Exception e) {
            return part;
        }
    }

    /**
     * URL 解码, Encode默认为UTF-8.
     */
    public static String urlDecode(String part) {
        if (StringUtils.isBlank(part)) {
            return "";
        }
        try {
            return URLDecoder.decode(part, DEFAULT_URL_ENCODING);
        } catch (Exception e) {
            return part;
        }
    }
}
