package com.needto.simulate.entity;

import com.needto.common.context.SpringEnv;
import com.needto.http.entity.HttpHeader;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;

/**
 * @author Administrator
 */
public class Driver {

    private static final HttpHeader DEFAULT_HEADERS = new HttpHeader();

    private WebDriver driver;

    public Driver init(String driverPath, HttpHeader headers, ProxyData proxyData, boolean loadImage){
        HttpHeader headerMap = DEFAULT_HEADERS;
        if(headers != null){
            headerMap = headers;
        }
        WebDriverBuilder webDriverBuilder = WebDriverBuilder.of(driverPath);
        String ua = headerMap.getUserAgent();
        if(StringUtils.isEmpty(ua)){
            webDriverBuilder.setUserAgent(WebDriverBuilder.DEFAULT_WEB_UA);
        }else{
            webDriverBuilder.setUserAgent(ua);
        }

        webDriverBuilder.setDisableImage(!loadImage);

        if(proxyData != null){
            Proxy proxyTemp = new Proxy();
            proxyTemp.setAutodetect(true);

            if(proxyData.getAddr().startsWith("http")){
                proxyTemp.setHttpProxy(proxyData.getAddr());
            }else if(proxyData.getAddr().startsWith("sock")){
                proxyTemp.setSocksProxy(proxyData.getAddr());
                proxyTemp.setSocksUsername(proxyData.getUsername());
                proxyTemp.setSocksPassword(proxyData.getPassword());
            }
        }
        driver = webDriverBuilder.build();
        return this;
    }

    public void stop(){
        if(this.driver != null){
            this.driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }
}
