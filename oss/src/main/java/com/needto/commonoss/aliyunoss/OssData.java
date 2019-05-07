package com.needto.commonoss.aliyunoss;


import com.needto.common.entity.Dict;

/**
 * @author Administrator
 * 阿里云oss参数
 */
public class OssData {

    /**
     * 回调数据
     */
    public static class CallBack{
        /**
         * 回调url
         */
        public String callbackUrl;
        /**
         * 发起回调请求时 Host 头的值
         */
        public String callbackHost;
        /**
         * body
         * 格式: key=$(key)&etag=$(etag)&my_var=$(x:my_var)
         */
        public String callbackBody;

        /**
         * body参数
         */
        public String callbackBodyType;

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public String getCallbackHost() {
            return callbackHost;
        }

        public void setCallbackHost(String callbackHost) {
            this.callbackHost = callbackHost;
        }

        public String getCallbackBody() {
            return callbackBody;
        }

        public void setCallbackBody(String callbackBody) {
            this.callbackBody = callbackBody;
        }

        public String getCallbackBodyType() {
            return callbackBodyType;
        }

        public void setCallbackBodyType(String callbackBodyType) {
            this.callbackBodyType = callbackBodyType;
        }
    }

    public String accessid;
    public String policy;
    public String signature;
    public String dir;
    public String host;
    public String expire;

    /**
     * 回调数据
     */
    public String callback;

    /**
     * 额外参数
     */
    public Dict extra;

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public Dict getExtra() {
        return extra;
    }

    public void setExtra(Dict extra) {
        this.extra = extra;
    }
}
