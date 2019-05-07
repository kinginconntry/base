package com.needto.common.entity;

import java.util.Map;

/**
 * @author Administrator
 * 小程序二维码配置
 */
public class MiniQrConfig {

    /**
     * 小尺寸二维码key
     */
    public static final String DEFAULT_WIDTH_SMALL_KEY = "s";

    /**
     * 小尺寸二维码宽度
     */
    public static final int DEFAULT_WIDTH_SMALL_LEN = 512;

    /**
     * 大尺寸二维码key
     */
    public static final String DEFAULT_WIDTH_LARGE_KEY = "l";

    /**
     * 大尺寸二维码宽度
     */
    public static final int DEFAULT_WIDTH_LARGE_LEN = 1024;

    /**
     * 小程序默认大小
     */
    public static final int DEFAULT_WIDTH = 430;

    /**
     * 二维码名称
     */
    public String imgName;

    /**
     * 大小类型
     */
    public String size;

    /**
     * 小程序页面路径
     */
    public String page;

    /**
     * 二维码宽度
     */
    public int width;

    /**
     * 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式
     */
    public String scene;

    /**
     * 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调
     */
    public boolean autoColor = false;

    /**
     * autoColor 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
     */
    public Map<String, String> lineColor;

    /**
     * 是否需要透明底色， isHyaline 为true时，生成透明底色的小程序码
     */
    public boolean isHyaline = false;
}
