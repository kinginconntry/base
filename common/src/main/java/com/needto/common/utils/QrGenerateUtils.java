package com.needto.common.utils;

import com.alibaba.fastjson.JSON;
import com.needto.common.entity.MiniQrConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
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

    private static final Logger logger = LoggerFactory.getLogger(QrGenerateUtils.class);

    /**
     * 生成二维码
     *
     * @param response    请求响应
     * @param download    是否需要下载到微信小程序中
     * @param accessToken 小程序生成二维码api调用的token
     * @param qrParam     小程序二维码参数
     * @throws IOException
     */
    public static void produceWeiXinMiniQr(HttpServletResponse response, boolean download, String accessToken, MiniQrConfig qrParam) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");

        if (qrParam == null || StringUtils.isEmpty(qrParam.page)) {
            logger.debug("小程序参数不正确，参数：{}", qrParam != null ? JSON.toJSONString(qrParam) : "无");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (StringUtils.isEmpty(accessToken)) {
            logger.debug("没有小程序token");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (qrParam.width == 0) {
            qrParam.width = MiniQrConfig.DEFAULT_WIDTH_SMALL_LEN;
        }

        if (StringUtils.isEmpty(qrParam.scene)) {
            qrParam.scene = Long.toString(System.currentTimeMillis());
        }

        if (MiniQrConfig.DEFAULT_WIDTH_SMALL_KEY.equals(qrParam.size)) {
            qrParam.width = MiniQrConfig.DEFAULT_WIDTH_SMALL_LEN;
        } else if (MiniQrConfig.DEFAULT_WIDTH_LARGE_KEY.equals(qrParam.size)) {
            qrParam.width = MiniQrConfig.DEFAULT_WIDTH_LARGE_LEN;
        }

        if (StringUtils.isEmpty(qrParam.imgName)) {
            if (qrParam.size == null) {
                qrParam.size = "s";
            }
            qrParam.imgName = qrParam.scene + "_" + qrParam.size + ".jpeg";
        }
        if (download) {
            logger.debug("下载小程序二维码");
            response.setHeader("Location", qrParam.imgName);
            response.setHeader("Content-Disposition", "attachment; filename=" + qrParam.imgName);
        }
        String url = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken;

        Map<String, Object> contentMap = new HashMap<>(6);
        contentMap.put("page", qrParam.page);
        contentMap.put("width", qrParam.width);
        contentMap.put("scene", qrParam.scene);
        contentMap.put("auto_color", qrParam.autoColor);
        contentMap.put("line_color", qrParam.lineColor);
        contentMap.put("is_hyaline", qrParam.isHyaline);

        RestTemplate client = new RestTemplate();
        ResponseEntity<Resource> entitys = client.postForEntity(url, HttpMethod.POST, Resource.class, contentMap);
        if (entitys.getStatusCode().equals(HttpStatus.OK) && entitys.getBody() != null) {
            InputStream in = entitys.getBody().getInputStream();
            BufferedImage bi = ImageIO.read(in);
            OutputStream out = response.getOutputStream();
            if(bi == null){
                logger.debug("没有获取到图片数据");
            }
            ImageIO.write(bi, "jpg", out);
        }else{
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
