package com.needto.tokenizer.entity;

import org.wltea.analyzer.cfg.Configuration;

import java.util.List;

public class IkConfig implements Configuration {
    /**
     * 分词器默认字典路径
     */
    private static final String PATH_DIC_MAIN = "org/wltea/analyzer/dic/main2012.dic";
    private static final String PATH_DIC_QUANTIFIER = "org/wltea/analyzer/dic/quantifier.dic";

    private boolean useSmart;

    private List<String> exe;

    private List<String> stop;

    @Override
    public boolean useSmart() {
        return useSmart;
    }

    @Override
    public void setUseSmart(boolean b) {
        this.useSmart = useSmart;
    }

    @Override
    public String getMainDictionary() {
        return PATH_DIC_MAIN;
    }

    @Override
    public String getQuantifierDicionary() {
        return PATH_DIC_QUANTIFIER;
    }

    @Override
    public List<String> getExtDictionarys() {
        return exe;
    }

    @Override
    public List<String> getExtStopWordDictionarys() {
        return stop;
    }

    public void setExe(List<String> exe) {
        this.exe = exe;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }
}
