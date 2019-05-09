package com.needto.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * 生成维码图片生成器工具类
 */
public class QrGenerateUtils {

    private static final Logger LOG = LoggerFactory.getLogger(QrGenerateUtils.class);

    /**
     * 微信小程序二维码生成url
     */
    private static final String MINI_URL = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=";

    /**
     * 生成普通二维码
     * @param out
     * @param qrParam
     */
    public static void createQr(OutputStream out, QrConfig qrParam){
        Assert.validateNull(qrParam, "NO_PARAM", "");
        qrParam.init();
    }

    /**
     * 生成二维码
     *
     * @param accessToken 小程序生成二维码api调用的token
     * @param qrParam     小程序二维码参数
     * @throws IOException
     */
    public static void createWechatMiniQr(OutputStream out, String accessToken, WechatMiniQrConfig qrParam) throws IOException {

        Assert.validateStringEmpty(accessToken, "NO_TOKEN", "");
        Assert.validateCondition(qrParam == null || StringUtils.isEmpty(qrParam.page), "NO_PARAM", "");

        qrParam.init();

        if (StringUtils.isEmpty(qrParam.scene)) {
            qrParam.scene = Long.toString(System.currentTimeMillis());
        }

        if (StringUtils.isEmpty(qrParam.name)) {
            if (qrParam.size == null) {
                qrParam.size = "s";
            }
            qrParam.name = qrParam.scene + "_" + qrParam.size + ".jpeg";
        }

        Map<String, Object> contentMap = new HashMap<>(6);
        contentMap.put("page", qrParam.page);
        contentMap.put("width", qrParam.width);
        contentMap.put("scene", qrParam.scene);
        contentMap.put("auto_color", qrParam.autoColor);
        contentMap.put("line_color", qrParam.lineColor);
        contentMap.put("is_hyaline", qrParam.isHyaline);

        RestTemplate client = new RestTemplate();
        ResponseEntity<Resource> entitys = client.postForEntity(MINI_URL + accessToken, HttpMethod.POST, Resource.class, contentMap);
        if (entitys.getStatusCode().equals(HttpStatus.OK) && entitys.getBody() != null) {
            InputStream in = entitys.getBody().getInputStream();
            BufferedImage bi = ImageIO.read(in);
            if(bi == null){
                LOG.debug("没有获取到图片数据");
            }
            ImageIO.write(bi, "jpg", out);
        }
    }

    /**
     * 普通二维码
     */
    public static class QrConfig {
        /**
         * 小尺寸二维码key
         */
        public static final String DEFAULT_WIDTH_SMALL_KEY = "s";

        /**
         * 大尺寸二维码key
         */
        public static final String DEFAULT_WIDTH_LARGE_KEY = "l";

        /**
         * 二维码宽度
         */
        public Integer width;

        /**
         * 大小类型
         */
        public String size;

        /**
         * 二维码名称
         */
        public String name;

        public void init(){
            if (this.width == null) {
                if(DEFAULT_WIDTH_SMALL_KEY.equalsIgnoreCase(size)){
                    this.width = 512;
                }else if(DEFAULT_WIDTH_LARGE_KEY.equalsIgnoreCase(size)){
                    this.width = 1024;
                }else{
                    this.width = 512;
                }
            }

            if(this.width <= 0){
                this.width = 512;
            }
        }
    }

    /**
     * @author Administrator
     * 小程序二维码配置
     */
    public static class WechatMiniQrConfig extends QrConfig {

        /**
         * 小程序页面路径
         */
        public String page;

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
}
