package com.needto.web.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class HttpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 生成普通二维码
     */
    public void createQr(HttpServletResponse response, QrGenerateUtils.QrConfig config, boolean download) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        if (download) {
            LOG.debug("下载小程序二维码");
            response.setHeader("Location", config.name);
            response.setHeader("Content-Disposition", "attachment; filename=" + config.name);
        }
        QrGenerateUtils.createQr(response.getOutputStream(), config);
    }

    /**
     * 微信小程序二维码
     */
    public void createWechatMiniQr(HttpServletResponse response, String accessToken, QrGenerateUtils.WechatMiniQrConfig config, boolean download) throws IOException {
        response.setHeader("Cache-Control", "no-store, no-cache");
        response.setContentType("image/jpeg");
        if (download) {
            LOG.debug("下载小程序二维码");
            response.setHeader("Location", config.name);
            response.setHeader("Content-Disposition", "attachment; filename=" + config.name);
        }
        QrGenerateUtils.createWechatMiniQr(response.getOutputStream(), accessToken, config);
    }
}
