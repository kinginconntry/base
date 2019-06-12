package com.needto.simulate.entity;

import com.needto.tool.entity.Dict;

/**
 * @author Administrator
 */
public class ProcessScript {

    /**
     * 是否注入jquery
     */
    private boolean injectJquery;

    /**
     * 脚本执行前的准备脚本，返回为true表示加载完成，可以执行下一步（循环检查，直到超时）
     */
    private String prepare;

    /**
     * 准备脚本参数
     */
    private Dict prepareArgs;

    /**
     * 脚本执行后的检查脚本，返回为true表示加载完成，可以执行下一步（循环检查，直到超时）
     */
    private String finish;

    /**
     * 检查脚本参数
     */
    private Dict finishArgs;

    /**
     * 等待超时：
     */
    private long timeout;

    /**
     * 执行脚本
     */
    public String script;

    /**
     * 执行脚本参数
     */
    private Dict args;


    public ProcessScript() {
        this.timeout = 60000;
    }

    public String getPrepare() {
        return prepare;
    }

    public ProcessScript setPrepare(String prepare) {
        this.prepare = prepare;
        return this;
    }

    public Dict getPrepareArgs() {
        return prepareArgs;
    }

    public ProcessScript setPrepareArgs(Dict prepareArgs) {
        this.prepareArgs = prepareArgs;
        return this;
    }

    public String getFinish() {
        return finish;
    }

    public ProcessScript setFinish(String finish) {
        this.finish = finish;
        return this;
    }

    public Dict getFinishArgs() {
        return finishArgs;
    }

    public ProcessScript setFinishArgs(Dict finishArgs) {
        this.finishArgs = finishArgs;
        return this;
    }

    public long getTimeout() {
        return timeout;
    }

    public ProcessScript setTimeout(long timeout) {
        if(timeout <= 0){
            return this;
        }
        this.timeout = timeout;
        return this;
    }

    public String getScript() {
        return script;
    }

    public ProcessScript setScript(String script) {
        this.script = script;
        return this;
    }

    public Dict getArgs() {
        return args;
    }

    public ProcessScript setArgs(Dict args) {
        this.args = args;
        return this;
    }

    public boolean isInjectJquery() {
        return injectJquery;
    }

    public ProcessScript setInjectJquery(boolean injectJquery) {
        this.injectJquery = injectJquery;
        return this;
    }
}
