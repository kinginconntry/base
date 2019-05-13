package com.needto.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 *
 * @author Administrator
 */
public class HttpUtil {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtil.class);

    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    /**
     * post下载文件
     * @param url
     * @param httpHeaders
     * @param body
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> InputStream postFile(String url, HttpHeaders httpHeaders, T body) throws IOException {
        return downFile(url, HttpMethod.POST, httpHeaders, body);
    }

    /**
     * post下载文件
     * @param url
     * @param body
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> InputStream postFile(String url, T body) throws IOException {
        return downFile(url, HttpMethod.POST, null, body);
    }

    /**
     * post下载文件
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream postFile(String url) throws IOException {
        return downFile(url, HttpMethod.POST, null, null);
    }

    /**
     * get下载文件
     * @param url
     * @param httpHeaders
     * @return
     * @throws IOException
     */
    public static InputStream getFile(String url, HttpHeaders httpHeaders) throws IOException {
        return downFile(url, HttpMethod.GET, httpHeaders, null);
    }

    /**
     * get下载文件
     * @param url
     * @return
     * @throws IOException
     */
    public static InputStream getFile(String url) throws IOException {
        return getFile(url, null);
    }


    /**
     * 下载
     * @param url
     * @param httpMethod
     * @param httpHeaders
     * @param body
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> InputStream downFile(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, T body) throws IOException {
        Assert.validateCondition(!ValidateUtils.isUrl(url));
        URI uri = URI.create(url);
        return downFile(uri, httpMethod, httpHeaders, body);
    }

    /**
     * 下载
     * @param uri
     * @param httpMethod
     * @param httpHeaders
     * @param body
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> InputStream downFile(URI uri, HttpMethod httpMethod, HttpHeaders httpHeaders, T body) throws IOException {
        Assert.validateNull(httpMethod);
        Assert.validateNull(uri);
        HttpResponse httpResponse;
        try {
            Request request;
            if(HttpMethod.GET.equals(httpMethod)){
                request = Request.Get(uri);
            }else{
                request = Request.Post(uri);
            }
            request.addHeader("user-agent", DEFAULT_USER_AGENT);
            if(httpHeaders != null){
                for(String key : httpHeaders.keySet()){
                    request.setHeader(new BasicHeader(key, httpHeaders.getFirst(key)));
                }
            }
            if(body != null){
                request.bodyString(JSON.toJSONString(body), ContentType.DEFAULT_TEXT);
            }
            httpResponse = request.execute().returnResponse();
        } catch (Exception e) {
            LOG.error("远程请求失败，url=" + uri, e);
            throw new FileNotFoundException();
        }

        int code = httpResponse.getStatusLine().getStatusCode();
        if (code != 200) {
            throw new FileNotFoundException();
        }

        return httpResponse.getEntity().getContent();
    }

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
