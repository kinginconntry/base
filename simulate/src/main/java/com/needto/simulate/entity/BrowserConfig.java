package com.needto.simulate.entity;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Dimension;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class BrowserConfig {

    /**
     * 自定义加载脚本链接
     */
    private List<String> loadScriptLinks;

    /**
     * 脚本列表
     */
    private List<ProcessScript> processScriptList;

    /**
     * 使用手机模拟配置
     */
    private boolean mobile;

    /**
     * 浏览器窗口大小
     */
    protected Dimension windowSize;

    /**
     * 加载图片
     */
    protected boolean loadImage;

    public boolean isLoadImage() {
        return loadImage;
    }

    public void setLoadImage(boolean loadImage) {
        this.loadImage = loadImage;
    }

    public List<ProcessScript> getProcessScriptList() {
        return processScriptList;
    }

    public void setProcessScriptList(List<ProcessScript> processScriptList) {
        this.processScriptList = processScriptList;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public void addProcessScript(ProcessScript processScript){
        if(processScript == null || StringUtils.isEmpty(processScript.getScript())){
            return;
        }
        if(this.processScriptList == null){
            this.processScriptList = new ArrayList<>();
        }
        this.processScriptList.add(processScript);
    }

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    public List<String> getLoadScriptLinks() {
        return loadScriptLinks;
    }

    public void setLoadScriptLinks(List<String> loadScriptLinks) {
        this.loadScriptLinks = loadScriptLinks;
    }
}
