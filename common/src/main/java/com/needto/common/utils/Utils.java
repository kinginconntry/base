package com.needto.common.utils;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.needto.common.exception.BaseException;
import com.needto.common.inter.IOrder;
import net.sf.json.xml.XMLSerializer;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.cglib.core.Converter;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * @author Administrator
 */
public class Utils {

    public static final ConcurrentHashMap<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 默认的key连接符
     */
    public static final String DEFAULT_SEPARATOR = ".";

    /**
     * 获取全局id（base64编码）
     * @return
     */
    public static String getGuidBase64() {
        return Base64.getEncoder().encodeToString(String.format("%s%s", getGuid(), System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取全局guid
     * @return
     */
    public static String getGuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将对象转化为空字符串
     * @param object
     * @return
     */
    public static String nullToString(Object object){
        if(object == null){
            return "";
        }else{
            return object.toString();
        }
    }

    /**
     * 强转一个对象，主要处理null
     * @param o
     * @param <T>
     * @return
     */
    public static <T> T getValue(Object o){
        return getValue(o, null);
    }

    public static <T> T getValue(Object o, T defVal){
        if(o == null){
            return defVal;
        }else{
            return (T) o;
        }
    }

    /**
     * 比较两个对象
     * @param object1
     * @param object2
     * @return
     */
    public static boolean equals(Object object1, Object object2){
        if(object1 == null && object2 == null){
            return true;
        }
        if(object1 != null && object2 != null){
            return object1.toString().equals(object2.toString());
        }
        return false;
    }

    /**
     * 产生随机字符串
     * @return
     */
    public static String randomStr(){
        return Base64.getEncoder().encodeToString(String.format("%s", System.currentTimeMillis()).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取当前时间戳
     * @return
     */
    public static long timestamp(){
        return System.currentTimeMillis();
    }

    /**
     * 混淆id  md5加密
     * @param str
     * @return
     */
    public static String confuseStr(String str){
        if(StringUtils.isEmpty(str)){
            return str;
        }
        return DigestUtils.md5DigestAsHex(str.getBytes());
    }

    /**
     * 获取本机IP
     */
    public static String getLocalIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * xml转对象
     * @param xmlStr
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T xmlToObject(String xmlStr, Class<T> tClass){
        if(StringUtils.isEmpty(xmlStr)){
            return null;
        }
        XMLSerializer xmlSerializer = new XMLSerializer();
        String resutStr = xmlSerializer.read(xmlStr).toString();
        return JSONObject.parseObject(resutStr, tClass);
    }

    /**
     * xml转json
     * @param xmlStr
     * @return
     */
    public static JSONObject xmlToJson(String xmlStr){
        if(StringUtils.isEmpty(xmlStr)){
            return null;
        }
        XMLSerializer xmlSerializer = new XMLSerializer();
        String resutStr = xmlSerializer.read(xmlStr).toString();
        return JSONObject.parseObject(resutStr);
    }

    /**
     * 转换map结构: 和es类似
     *
     * 原始结构
     * {
     *     "key1": {
     *         "key2": 1
     *     }
     * }
     * 转换后的结构
     * {
     *     "key1.key2": 1
     * }
     *
     * @param map
     * @return
     */
    public static Map<String, Object> transferDeepMapToHorizontal(Map<String, Object> map, String separator){
        if(map == null){
            return null;
        }
        if(StringUtils.isEmpty(separator)){
            separator = DEFAULT_SEPARATOR;
        }
        Map<String, Object> resMap = new HashMap<>();
        for(Map.Entry<String, Object> temp : map.entrySet()){
            String key = temp.getKey();
            if(StringUtils.isEmpty(key)){
                continue;
            }
            Object val = temp.getValue();
            if(val instanceof Map){
                Map<String, Object> tempMap = transferDeepMapToHorizontal((Map<String, Object>) val, separator);
                if(tempMap == null){
                    continue;
                }
                for(String tempMapKey : tempMap.keySet()){
                    resMap.put(key + separator + tempMapKey, tempMap.get(tempMapKey));
                }
            }else{
                resMap.put(key, val);
            }
        }
        return resMap;
    }

    public static String trim(String obj){
        if(obj == null){
            return null;
        }
        return obj.trim();
    }

    private static String generateKey(Class<?>class1, Class<?>class2){
        return class1.toString() + class2.toString();
    }

    /**
     * 通过cglib的方式来进行对象赋值，其性能最优，但不支持名字相同类型不同的情况
     * @Description
     * @author Administrator
     * @param source
     * @param target
     */
    public static  void copyPropertiesOfCglibBean(Object source,Object target){
        copyPropertiesOfCglibBean(source, target, null);
    }

    /**
     * 通过cglib的方式来进行对象赋值，其性能最优，但不支持名字相同类型不同的情况
     * @Description
     * @author Administrator
     * @param source
     * @param target
     */
    public static void copyPropertiesOfCglibBean(Object source,Object target, Converter converter){
        String beanKey = generateKey(source.getClass(),target.getClass());
        BeanCopier copier;
        if (!BEAN_COPIER_MAP.containsKey(beanKey)) {
            copier = BeanCopier.create(source.getClass(), target.getClass(), null != converter);
            BEAN_COPIER_MAP.put(beanKey, copier);
        }else {
            copier = BEAN_COPIER_MAP.get(beanKey);
        }
        copier.copy(source, target, converter);
    }


    /**
     * 根据分隔符切割字符串
     * @param str
     * @param sep
     * @return
     */
    public static List<String> getList(String str, String sep){
        if(str == null || sep == null){
            return new ArrayList<>(0);
        }
        String[] args = str.split(sep);
        return Lists.newArrayList(args);
    }

    /**
     * 按照都好切割字符串
     * @param str
     * @return
     */
    public static List<String> getList(String str){
        return getList(str, ",");
    }


    /**
     * 获取url协议
     * @param url
     * @return
     */
    public static String getUrlProtocol(String url){

        if(!ValidateUtils.isUrl(url)){
            throw new BaseException("INVLID_URL", "");
        }
        return url.substring(0, url.indexOf("://"));
    }

    /**
     * 获取url主机（域名+端口）
     * @param url
     * @return
     */
    public static String getUrlHost(String url){
        if(!ValidateUtils.isUrl(url)){
            throw new BaseException("INVLID_URL", "");
        }
        String temp = url.substring(url.indexOf("://") + 3);
        int next = temp.indexOf("/");
        if(next <= -1){
            return temp;
        }else{
            return temp.substring(0, next);
        }
    }

    /**
     * 获取域名
     * @param url
     * @return
     */
    public static String getUrlDomain(String url){
        String temp = getUrlHost(url);
        int index = temp.indexOf(":");
        if(index > -1){
            return temp.substring(0, index);
        }
        return temp;
    }

    /**
     * 获取url端口
     * @param url
     * @return
     */
    public static String getUrlPort(String url){
        String temp = getUrlHost(url);
        int index = temp.indexOf(":");
        if(index > -1){
            return temp.substring(index + 1);
        }else{
            return "80";
        }
    }

    /**
     *
     * @param url
     * @param ifParam 是否需要url参数
     * @return
     */
    public static String getUrlPath(String url, boolean ifParam){
        if(!ValidateUtils.isUrl(url)){
            throw new BaseException("INVLID_URL", "");
        }
        String temp = url.substring(0, url.indexOf("://"));
        int index = temp.indexOf("/");
        if(index <= -1){
            return "";
        }
        String path = temp.substring(index);
        if(!ifParam){
            int q = path.indexOf("?");
            if(q > -1){
                path = path.substring(0, q);
            }
        }
        return path;
    }

    /**
     * 为url设置参数
     * @param url
     * @param params
     * @return
     */
    public static String setUrlParam(String url, Map<String, String> params){
        Assert.validateNull(url);
        if(!CollectionUtils.isEmpty(params)){
            StringBuilder paramStr = new StringBuilder();
            for(Map.Entry<String, String> entry : params.entrySet()){
                paramStr.append(entry.getKey()).append("=").append(Utils.nullToString(entry.getValue())).append("&");
            }
            if(!url.contains("?")){
                url += "?";
            }
            url += paramStr.substring(0, paramStr.length() - 1);
        }
        return url;
    }

    /**
     * 将某个字符串按照正则进行匹配，将匹配到的内容换成提供的内容（主要用于替换占位符）
     * @param source 原始数据
     * @param reg 匹配正则
     * @param params 需要替换的内容集合
     * @return
     */
    public static String replaceReg(String source, String reg, List<String> params){

        if(source == null || reg == null || params == null){
            return source;
        }
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(source);
        int size = matcher.groupCount();
        Assert.validateCondition(size != params.size(), "PARAM_MATCH_FAILTURE", "replace can not match size of params");
        for(int i = 0; i < size; i++){
            String group = matcher.group(i);
            source = source.replaceFirst(group, params.get(i));
        }
        return source;
    }

    /**
     * 排序 IOrder 接口
     * @param orders
     * @param desc
     */
    public static void orderSort(List<IOrder> orders, boolean desc) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        if (!desc) {
            // 正序
            orders.sort((a, b) -> {
                if (a.getOrder() > b.getOrder()) {
                    return 1;
                } else if (a.getOrder() > b.getOrder()) {
                    return -1;
                } else {
                    return 0;
                }
            });
        } else {
            // 逆序
            orders.sort((a, b) -> {
                if (a.getOrder() > b.getOrder()) {
                    return -1;
                } else if (a.getOrder() > b.getOrder()) {
                    return 1;
                } else {
                    return 0;
                }
            });
        }
    }

        /**
         * 排序 IOrder 接口, 正序
         * @param orders
         */
    public static void orderSort(List<IOrder> orders) {
        orderSort(orders, false);
    }

    /**
     * 获取spring带有共同前缀的配置
     * @param environment
     * @param commonPrefix
     * @return
     */
    public static Map<String, Map<String, Object>> getProperties(AbstractEnvironment environment, String commonPrefix){
        MutablePropertySources propertySources = environment.getPropertySources();
        Map<String, Map<String, Object>> map = new HashMap<>();
        propertySources.forEach(propertySource -> {
            if (propertySource instanceof MapPropertySource) {
                MapPropertySource mps = (MapPropertySource) propertySource;
                Set<String> keys = mps.getSource().keySet();
                for (String key : keys) {
                    if (key.startsWith(commonPrefix)) {
                        String emailtemp = key.replace(commonPrefix, "");
                        int index = emailtemp.indexOf(".");
                        String prefix = "";
                        if(index > -1 && index < (emailtemp.length() - 1)){
                            prefix = emailtemp.substring(0, index);
                        }
                        if(!StringUtils.isEmpty(prefix)){
                            if(!map.containsKey(prefix)){
                                map.put(prefix, new HashMap<>());
                            }
                            String temp = emailtemp.replace(prefix + ".", "");
                            map.get(prefix).put(temp, mps.getProperty(key));
                        }
                    }
                }
            }
        });
        return map;
    }

    public static void main(String[] args){

    }
}
