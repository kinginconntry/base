package com.needto.simulate.entity;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;

import static org.openqa.selenium.chrome.ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY;

/**
 * @author Administrator
 */

public class WebDriverBuilder {

    /**
     * 网站ua
     */
    public static final String DEFAULT_WEB_UA = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36";

    /**
     * 手机ua
     */
    public static final String DEFAULT_MOBILE_UA = "Mozilla/5.0 (Linux; Android 8.1.0; ALP-AL00 Build/HUAWEIALP-AL00; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/63.0.3239.83 Mobile Safari/537.36 T7/10.13 baiduboxapp/10.13.0.11 (Baidu; P1 8.1.0)";

    /**
     * 代理
     */
    private Proxy proxy;

    /**
     * 可执行文件路径
     */
    private String path;

    /**
     * 禁用图片加载
     */
    private boolean disableImage;

    /**
     * ua
     */
    private String userAgent;

    /**
     * 禁用沙盒模式
     */
    private boolean disableSandbox;

    /**
     * 禁用脚本
     *
     */
    private boolean disableJs;

    public WebDriverBuilder(String path){
        this.path = path;
        this.userAgent = DEFAULT_WEB_UA;
    }

    public static WebDriverBuilder of(String path){
        return new WebDriverBuilder(path);
    }

    public WebDriverBuilder setDisableImage(boolean disableImage) {
        this.disableImage = disableImage;
        return this;
    }

    public WebDriverBuilder setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public WebDriverBuilder setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public WebDriverBuilder setDisableJs(boolean disableJs) {
        this.disableJs = disableJs;
        return this;
    }

    public WebDriverBuilder setDisableSandbox(boolean disableSandbox) {
        this.disableSandbox = disableSandbox;
        return this;
    }

    public WebDriver buildChrome(){

        System.setProperty(CHROME_DRIVER_EXE_PROPERTY, path);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu");
        options.addArguments("lang=zh_CN.UTF-8");
        if(this.disableSandbox){
            options.addArguments("--no-sandbox");
        }
        if(this.disableJs){
            options.addArguments("--disable-javascript");
        }
        // 禁用所有插件
        options.addArguments("--disable-plugins");
        options.addArguments("--disable-dev-shm-usage");
        // 关闭使用 ChromeDriver 打开浏览器时上部提示语 "Chrome正在受到自动软件的控制"
        options.addArguments("disable-infobars");
        // 允许浏览器重定向，Framebusting requires same-origin or a user gesture
        options.addArguments("disable-web-security");
//        options.addArguments("--headless");
        options.addArguments("--user-agent="+this.userAgent);
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        // prefs.put("profile.default_content_settings.popups", 0);

        if(this.disableImage){
            //禁止下载加载图片
            prefs.put("profile.managed_default_content_settings.images",2);
        }

        options.setExperimentalOption("prefs", prefs);
        if(this.proxy != null){
            options.setCapability("proxy", proxy);
        }
        return new ChromeDriver(options);
    }

    public WebDriver build(){
        return buildChrome();
    }
}
