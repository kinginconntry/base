package com.needto.common.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * 验证工具
 */
public class ValidateUtils {

    /**
     * 身份证正则
     */
    private static final Pattern IDCARD_PATTERN = Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)");

    /**
     * url正则
     */
    private static final Pattern URL_PATTERN = Pattern.compile("(http|https?|ftp|file|)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");

    private static final String[] PAD_AGENTS = {"ipad"};
    private static final String[] MOBILE_AGENTS = {"iphone", "android", "micromessenger", "phone", "mobile", "wap", "opera mobi",
            "opera mini", "ucweb", "windows ce", "symbian", "webos", "sony", "blackberry", "dopod", "googlebot-mobile", "playbook",
            "nokia", "samsung", "ktouch", "meizu", "huawei", "coolpad", "midp", "cldc", "motorola"};

    /**
     * 邮箱正则
     */
    public static final Pattern EMAIL_REGEX = Pattern.compile("^(\\w)+([\\.\\-]\\w+)*@([\\w\\-])+(([\\.\\-]\\w{2,10}){1,3})$");

    /**
     * 手机号码正则
     * 移动：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * 联通：130 131 132 145 155 156 166 171 175 176 185 186
     * 电信：133 149 153 173 177 180 181 189 199
     * 虚拟运营商: 170
     */
    public static final Pattern MOBILE_NUMBER_PATTERN = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(19[0-9])|(18[0-9])|(17[0-9]))\\d{8}$");

    /**
     * 固定电话号码格式正则，简单识别为 国内区号-号码 不支持国际区号，不支持分机号
     */
    public static final Pattern TEL_NUMBER_PATTERN = Pattern.compile("^0[1-9]{2,3}-[0-9]{6,8}$");

    /**
     * 检查是否Email地址
     * @param s
     * @return
     */
    public static boolean isEmail(Object s) {
        if ("".equals(Utils.nullToString(s))) {
            return false;
        }
        Matcher matcher = EMAIL_REGEX.matcher(s.toString());
        return matcher.matches();
    }
    /**
     * 验证是否手机号码
     *
     * @param s
     * @return
     */
    public static boolean isMobile(String s) {
        if ("".equals(Utils.nullToString(s))) {
            return false;
        }
        if(s.startsWith("+") && s.length()>9){
            return true;
        }

        Matcher m = MOBILE_NUMBER_PATTERN.matcher(s);
        return m.matches();
    }

    /**
     * 验证是否是固定电话号码，注意，有可能手机号码也会被识别成电话号码。
     * @param s
     * @return
     */
    public static boolean isTel(String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        return TEL_NUMBER_PATTERN.matcher(s).matches();
    }

    /**
     * 判断用户是否通过移动端访问
     * @return
     */
    public static boolean isMoblie(String useragent) {
        for (String mobileAgent : MOBILE_AGENTS) {
            if (useragent.contains(mobileAgent)) {
                return true;
            }
        }
        //如果是平板，直接当PC处理
        for (String padAgent : PAD_AGENTS) {
            if (useragent.contains(padAgent)) {
                return true;
            }
        }
        return false;
    }

    public static String getDevice(String useragent){
        for (String mobileAgent : MOBILE_AGENTS) {
            if (useragent.contains(mobileAgent)) {
                return "MOBILE";
            }
        }
        //如果是平板，直接当PC处理
        for (String padAgent : PAD_AGENTS) {
            if (useragent.contains(padAgent)) {
                return "PAD";
            }
        }
        return "WEB";
    }

    /**
     *
     * 判断是否微信(含电脑版微信， 不含企业微信)
     * 如果需要判断是否手机微信，可通过组合isMobile来判断
     *
     * @param useragent
     * @return
     */
    public static boolean isWeixin(String useragent) {
        return useragent.contains("micromessenger") && !useragent.contains("wxwork");
    }

    /**
     *
     * 判断是否企业微信,含电脑版、手机版
     * 如果需要判断是否手机微信，可通过组合isMobile来判断
     * @param useragent
     * @return
     */
    public static boolean isQiyeWeixin(String useragent) {
        return useragent.contains("micromessenger") && useragent.contains("wxwork");
    }

    /**
     * 校验url
     * @param url
     * @return
     */
    public static boolean isUrl(String url){
        if(StringUtils.isEmpty(url)){
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }

    /**
     * 验证身份证
     * @param str
     * @return
     */
    public static boolean isIdCard(String str){
        if(StringUtils.isEmpty(str)){
            return false;
        }
        return IDCARD_PATTERN.matcher(str).matches();
    }

    /**
     * 验证字符串长度是否大于等于start，且小于等于end
     * @param str
     * @param start
     * @param end
     * @return
     */
    public static boolean validateStringLen(String str, int start, int end){
        if(str == null){
            return false;
        }

        return str.length() >= start && str.length() <= end;
    }

    /**
     * 校验正则
     * @param text
     * @param pattern
     * @return
     */
    public static boolean validateRegex(String text, String pattern){
        Assert.validateStringEmpty(text);
        Assert.validateStringEmpty(pattern);
        return Pattern.compile(pattern).matcher(text).matches();
    }

    /**
     * 判断一个字符串是否含有中文
     * @param str
     * @return
     */
    public static boolean containChinese(String str) {
        if (str == null){
            return false;
        }
        for (char c : str.toCharArray()) {
            // 根据字节码判断， 有一个中文字符就返回
            if (c >= 0x4E00 &&  c <= 0x9FA5){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args){
        String url = "://fasdf";
        System.out.println(isUrl(url));
    }
}
