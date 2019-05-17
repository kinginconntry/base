package com.needto.asr.aliyun;

import com.alibaba.idst.nls.NlsClient;
import com.alibaba.idst.nls.NlsFuture;
import com.alibaba.idst.nls.event.NlsListener;
import com.alibaba.idst.nls.protocol.NlsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author Administrator
 * @date 2018/5/31 0031
 * 阿里云语音转换连接客户端(长连接，单例)
 */

@Component
public class AsrClient {

    protected final static Logger LOG = LoggerFactory.getLogger(AsrClient.class);

    private NlsClient client;

    @PostConstruct
    public void initMethod() {
        if (client == null) {
            client = new NlsClient();
        }
        client.init();

        LOG.debug("阿里云语音识别客户端初始化完成");
    }

    @PreDestroy
    public void beforeDestory() {
        this.shutDown();
        LOG.debug("阿里云语音识别客户端已销毁");
    }

    public void shutDown() {
        // 关闭客户端并释放资源
        client.close();
    }

    public NlsClient getClient() {
        return client;
    }

    public NlsFuture createNlsFuture(NlsRequest req, NlsListener listener) throws Exception {
        return this.client.createNlsFuture(req, listener);
    }
}
