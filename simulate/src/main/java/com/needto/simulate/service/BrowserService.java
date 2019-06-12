package com.needto.simulate.service;

import com.needto.http.entity.Cookie;
import com.needto.simulate.data.JsResultCons;
import com.needto.simulate.data.SimulateResponseEvent;
import com.needto.simulate.entity.*;
import com.needto.tool.entity.Dict;
import com.needto.tool.exception.BaseException;
import com.needto.tool.exception.LogicException;
import com.needto.tool.utils.Assert;
import com.needto.tool.utils.ReflectUtils;
import com.needto.tool.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * TODO 这里需要使用http代理处理js文件，处理js文件中含有selenium的信息
 * @author Administrator
 */
@Service
public class BrowserService {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ValidateCodeContainer validateCodeContainer;


    public void asyncExecute(Request request){
        Response response = this.execute(request, true);
        SimulateResponseEvent simulateResponseEvent = new SimulateResponseEvent(this, response);
        this.applicationContext.publishEvent(simulateResponseEvent);
    }

    public Response syncExecute(Request request){
        return this.execute(request, false);
    }

    public Response execute(Request request, boolean async){

        BrowserConfig browserConfig = request.getBrowserConfig();
        Response response;
        if(browserConfig == null){
            response = new Response();
            response.setStatuscode(Response.FAILTURE);
            response.setError("NO_BROWSERCONFIG");
            return response;
        }
        if(browserConfig.isMobile()){
            if(browserConfig.getWindowSize() == null){
                browserConfig.setWindowSize(WindowSize.MOBILE_375.getDimension());
            }
        }else{
            if(browserConfig.getWindowSize() == null){
                browserConfig.setWindowSize(WindowSize.WEB_1920.getDimension());
            }
        }

        long startTime = System.currentTimeMillis();
        Driver driver = null;
        try {
            driver = new Driver();
            WebDriver webDriver = driver.init(request.getHeaders(), request.getProxyData(), browserConfig.isLoadImage()).getDriver();
            webDriver.manage().window().setSize(browserConfig.getWindowSize());
            response = new Response();

            // 请求页面
            webDriver.get(request.getUrlInfo().getUrl());
            // 等待页面加载，默认超时120秒
            webDriver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);

            // 加载自定义脚本
            if(!CollectionUtils.isEmpty(browserConfig.getLoadScriptLinks())){
                for(String link : browserConfig.getLoadScriptLinks()){
                    injectScript((JavascriptExecutor) driver, link);
                }
            }

            // 执行脚本
            if(!CollectionUtils.isEmpty(browserConfig.getProcessScriptList())){
                ProcessResult processResult = null;
                for(ProcessScript processScript : browserConfig.getProcessScriptList()){
                    if(processScript == null){
                        continue;
                    }
                    Object lastProcessResult = processResult == null ? null : processResult.getLastProcessRes();
                    Dict nextProcessResult = processResult == null ? null : processResult.getNextProcessData();
                    processResult = processScript(webDriver, processScript, lastProcessResult, nextProcessResult, async);
                    if(processResult == null){
                        throw new LogicException("", "");
                    }
                    response.addCookie(processResult.getCookies());
                    response.addHeader(processResult.getHeaders());
                }
            }

            // 返回最终响应数据
            response.setStatuscode(Response.SUCCESS);
            response.setRequest(request);
            response.setBody(webDriver.getPageSource());
        }catch (BaseException e){
            e.printStackTrace();
            response = new Response();
            response.setStatuscode(Response.FAILTURE);
            response.setError(e.getErrCode());
        } finally {
            if(driver != null){
                driver.stop();
            }
        }
        long endTime = System.currentTimeMillis();
        response.setRequestStartTime(startTime);
        response.setRequestEndTime(endTime);
        return response;
    }

    private ProcessResult processScript(WebDriver driver, ProcessScript processScript, Object lastProcessResult, Dict nextProcessResult, boolean async){
        // 注入
        if(processScript.isInjectJquery()){
            injectJquery((JavascriptExecutor) driver);
        }

        // 执行脚本之前的检查工作
        if(!StringUtils.isEmpty(processScript.getPrepare())){
            boolean prepareCheckFlag = false;
            long timeout = processScript.getTimeout();
            int i = 0;
            while (i < timeout){
                Object prepareRes;
                if(async){
                    prepareRes = ((JavascriptExecutor) driver).executeScript(processScript.getPrepare(), processScript.getPrepareArgs(), lastProcessResult);

                }else{
                    prepareRes = ((JavascriptExecutor) driver).executeScript(processScript.getPrepare(), processScript.getPrepareArgs(), lastProcessResult);

                }
                boolean check = (prepareRes instanceof String && "TRUE".equalsIgnoreCase(prepareRes.toString()))
                        || (prepareRes instanceof Boolean && (Boolean) prepareRes);
                if(check){
                    prepareCheckFlag = true;
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i += 2000;
            }
            Assert.validateCondition(!prepareCheckFlag, "PREPARE_CHECK_FAILTURE", "");
        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 执行脚本
        Object jsRes;
        if(async){
            jsRes = ((JavascriptExecutor) driver).executeScript(processScript.getScript(), processScript.getArgs(), lastProcessResult);
        }else{
            jsRes = ((JavascriptExecutor) driver).executeScript(processScript.getScript(), processScript.getArgs(), lastProcessResult);
        }

        ProcessResult processScriptTemp = getProcessResult(driver, jsRes);

        // 脚本是否完成的检查操作
        if(!StringUtils.isEmpty(processScript.getFinish())){
            boolean finishFlag = false;
            long timeout = processScript.getTimeout();
            int i = 0;
            while (i < timeout){
                Object finishRes;
                if(async){
                    finishRes = ((JavascriptExecutor) driver).executeScript(processScript.getFinish(), processScript.getFinishArgs(), lastProcessResult);
                }else{
                    finishRes = ((JavascriptExecutor) driver).executeScript(processScript.getFinish(), processScript.getFinishArgs(), lastProcessResult);
                }
                boolean check = (finishRes instanceof String && "TRUE".equalsIgnoreCase(finishRes.toString()))
                        || (finishRes instanceof Boolean && (Boolean) finishRes);
                if(check){
                    finishFlag = true;
                    break;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i += 2000;
            }
            Assert.validateCondition(!finishFlag, "FINISH_CHECK_FAILTURE", "");

        }else{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ProcessResult res = getProcessResult(driver, jsRes);
        if(processScriptTemp != null){
            res.addCookies(processScriptTemp.getCookies());
            res.addHeaders(processScriptTemp.getHeaders());
        }
        return res;
    }

    private ProcessResult getProcessResult(WebDriver driver, Object jsRes){
        ProcessResult processResult = new ProcessResult();
        processResult.setLastProcessRes(jsRes);
        if(jsRes instanceof Map){
            Map map = (Map) jsRes;
            Object jsheader = map.get(JsResultCons.HEADERS);
            if(jsheader instanceof Map){
                Map jsHeaderMap = (Map) jsheader;
                Map<String, String> headers = new HashMap<>(jsHeaderMap.size());
                for(Object key : jsHeaderMap.keySet()){
                    headers.put(Utils.nullToString(key.toString()), Utils.nullToString(jsHeaderMap.get(key)));
                }
                processResult.setHeaders(headers);
            }

            Object jsvdcode = map.get(JsResultCons.VALIDATE_REG);
            if(jsvdcode instanceof Map){
                Map jsCodeMap = (Map) jsvdcode;
                ValidateData validateData = new ValidateData();
                validateData = ReflectUtils.mapToBean((Map<String, Object>) jsCodeMap, validateData);
                IValidateCode iValidateCode = validateCodeContainer.get(validateData.getType());
                Assert.validateNull(iValidateCode, "NO_SUPPORT", "");
                String result = iValidateCode.getValidateCode(validateData.getGuid(), validateData.getData());
                validateData.setCodeRes(result);
                processResult.addNextProcessData(JsResultCons.VALIDATE_REG_RES, validateData);
            }
        }

        Set<org.openqa.selenium.Cookie> seleniumcookies = driver.manage().getCookies();
        if(!CollectionUtils.isEmpty(seleniumcookies)){
            List<Cookie> cookies = new ArrayList<>(seleniumcookies.size());
            for(org.openqa.selenium.Cookie temp : seleniumcookies){
                Cookie cookie = new Cookie(temp.getName(),temp.getValue(), temp.getDomain(),temp.getPath(),temp.getExpiry(),temp.isSecure(), temp.isHttpOnly());
                cookies.add(cookie);
            }
            processResult.setCookies(cookies);
        }
        return processResult;
    }

    /**
     * 注入jquery脚本
     * @param driver
     */
    private void injectJquery(JavascriptExecutor driver){
        injectScript(driver, "http://libs.baidu.com/jquery/2.0.0/jquery.min.js");
    }


    /**
     * 通过注入脚本
     * @param driver
     */
    private void injectScript(JavascriptExecutor driver, String link) {
        if(StringUtils.isEmpty(link)){
            return;
        }
        if(!scriptIsLoaded(driver)){
            driver.executeScript(" var headID = "

                    + "document.getElementsByTagName(\"head\")[0];"
                    + "var newScript = document.createElement('script');"
                    + "newScript.type = 'text/javascript';" + "newScript.src = "
                    + "'" + link + "';"
                    + "headID.appendChild(newScript);");
            boolean flag = false;
            int i = 0;
            int timeout = 60;
            while (true){
                if(scriptIsLoaded(driver)){
                    flag = true;
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(i > timeout){
                    break;
                }
                i += 1;
            }
            Assert.validateCondition(!flag, "LOAD_JQUERY_TIMEOUT", "");
        }
    }

    /**
     * 判断是否加载jquery
     * 返回true表示已加载jquery
     */
    private static Boolean scriptIsLoaded(JavascriptExecutor driver){
        Boolean loaded;
        try{
            loaded = (Boolean) driver.executeScript("return jQuery()!=null");
        }catch(Exception e){
            loaded = false;
        }
        return loaded;
    }
}
