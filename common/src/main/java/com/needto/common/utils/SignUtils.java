package com.needto.common.utils;

import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author Administrator
 * 签名
 */
public class SignUtils {

    /**
     * url签名key
     */
    public static final String _SG_KEY = "_sg";

    /**
     * 签名的时间戳
     */
    public static final String _TIME_STAMP = "_ts";

    /**
     * 签名携带的随机数
     */
    public static final String _NONCE_STR = "_ns";

    /**
     * 签名参数key
     */
    public static final String _SIGN_KEY_ = "_sign";

    /**
     * 获取参数中的随机数
     *
     * @param map
     * @return
     */
    public static String getNonceStr(Map<String, Object> map) {
        if (map == null) {
            return null;
        }
        return Utils.nullToString(map.get(_NONCE_STR));
    }

    /**
     * 签名url
     *
     * @param url    url
     * @param param  json参数
     * @param secret 密钥
     * @return 签名url （null表示参数不对）
     */
    public static String signUrl(String url, Map<String, Object> param, String secret) {
        if (url.contains("?")) {
            url += String.format("&%s=%s&%s=%s", _NONCE_STR, Utils.randomStr(), _TIME_STAMP, Utils.timestamp());
        } else {
            url += String.format("?%s=%s&%s=%s", _NONCE_STR, Utils.randomStr(), _TIME_STAMP, Utils.timestamp());
        }
        Map<String, Object> urlParam = RequestUtil.getUrlParam(url);
        if (urlParam == null) {
            return null;
        }
        if (param != null) {
            urlParam.putAll(param);
        }
        String sign = getParamSign(urlParam, secret);
        if (url.contains("?")) {
            url += String.format("&%s=%s", _SG_KEY, sign);
        } else {
            url += String.format("?%s=%s", _SG_KEY, sign);
        }
        return url;
    }

    public static String signUrl(String url, String secret) {
        return signUrl(url, null, secret);
    }

    /**
     * 校验url是否被签名
     *
     * @param url    url
     * @param param  参数json
     * @param secret 密钥
     * @return 是否被签名
     */
    public static boolean validateUrlSign(String url, Map<String, Object> param, String secret) {
        Map<String, Object> urlParam = RequestUtil.getUrlParam(url);
        if (urlParam == null) {
            return false;
        }
        if (param != null) {
            urlParam.putAll(param);
        }
        return validateParamSign(urlParam, secret);
    }

    public static boolean validateUrlSign(String url, String secret) {
        return validateUrlSign(url, null, secret);
    }

    /**
     * 校验参数签名
     *
     * @param map    参数
     * @param secret 密钥
     * @return 是否签名
     */
    public static boolean validateParamSign(Map<String, Object> map, String secret) {
        if (map == null) {
            return false;
        }
        String sign = Utils.nullToString(map.remove(_SG_KEY));
        if (StringUtils.isEmpty(sign)) {
            // 没有签名
            return false;
        }
        String checkSign = getParamSign(map, secret);
        return sign.equals(checkSign);
    }

    /**
     * 校验参数中的时间戳是否在某个时间内
     *
     * @param map 参数
     * @param ms  毫秒
     * @return
     */
    public static boolean validateParamTimeStamp(Map<String, Object> map, long ms) {
        if (map == null) {
            return false;
        }
        long t = -1;
        if(map.get(_TIME_STAMP) != null){
            t = Long.valueOf(Utils.nullToString(map.get(_TIME_STAMP)));
        }
        if (t > 0 && (Utils.timestamp() - t) < ms) {
            return true;
        }
        return false;
    }

    /**
     * 根据参数进行
     *
     * @param map    参数
     * @param secret 密钥
     * @return 签名值（null表示参数不对）
     */
    public static String getParamSign(Map<String, Object> map, String secret) {
        Assert.validateNull(map, "sign map can not be null");
        String sign = null;
        map.put(_SIGN_KEY_, secret);
        List<String> keys = new ArrayList<>(map.keySet());
        Collections.sort(keys);
        StringBuilder stringBuilder = new StringBuilder();
        keys.forEach((key) -> {
            stringBuilder.append(String.format("%s=%s&", key, Utils.nullToString(map.get(key))));
        });

        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            sign = Crypto.BASE64.encry(Crypto.MD5.encry(stringBuilder.toString(), ""), "");
        }
        return sign;
    }
}
