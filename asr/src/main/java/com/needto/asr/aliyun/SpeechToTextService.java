package com.needto.asr.aliyun;

import com.alibaba.idst.nls.NlsFuture;
import com.alibaba.idst.nls.protocol.NlsRequest;
import com.needto.common.utils.IOUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author Administrator
 * @date 2018/5/31 0031
 * 阿里云语音识别组件（一句话识别）
 */
@Component
public class SpeechToTextService {

    protected final static Logger LOG = LoggerFactory.getLogger(SpeechToTextService.class);

    /**
     * 请求的默认超时时间：毫秒
     */
    private final static int DEFAULT_TIMEOUT = 20000;

    /**
     * 可设置的默认最大超时时间：毫秒
     */
    private final static int MAX_TIMEOUT = 60000;

    /**
     * 语音appKey
     */
    @Value("${asr.appKey}")
    private String appKey;

    /**
     * 语音accessKeyId
     */
    @Value("${asr.accessKeyId}")
    private String accessKeyId;

    /**
     * 语音accessKeySecret
     */
    @Value("${asr.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 语音连接客户端
     */
    @Autowired
    private AsrClient asrClient;

    /**
     * 开启一个语音转换任务
     *
     * @param in      语音内容输入流
     * @param timeout  任务处理超时
     * @return
     */
    public AsrResult callAsrService(InputStream in, int timeout) {
        LOG.debug("calling NLS service start");
        if (in == null) {
            return AsrResult.forError("语音数据不能为空");
        }

        if (timeout <= 0) {
            timeout = DEFAULT_TIMEOUT;
        } else if (timeout > MAX_TIMEOUT) {
            timeout = MAX_TIMEOUT;
        }

        SpeechToTextCall listener = new SpeechToTextCall();

        try {
            NlsRequest req = new NlsRequest();
            req.setAppKey(appKey);
            // 设置语音文件格式为pcm,我们支持16k 16bit 的无头的pcm文件。
            req.setAsrFormat("pcm");
            req.authorize(accessKeyId, accessKeySecret);
            NlsFuture future = asrClient.createNlsFuture(req, listener);


            byte[] b = new byte[8000];
            int len = 0;
            // 发送语音数据
            while ((len = in.read(b)) > 0) {
                future.sendVoice(b, 0, len);
            }
            IOUtil.close(in);
            // 语音识别结束时，发送结束符
            future.sendFinishSignal();
            // 设置服务端结果返回的超时时间
            future.await(timeout);
            return  listener.getResult();

        } catch (Exception e) {
            LOG.error("NlsRequest error", e);
            return AsrResult.forError(e.toString());
        }
    }

    /**
     * 使用默认超时
     * @param in
     * @return
     */
    public AsrResult callAsrService(InputStream in){
        return this.callAsrService(in, DEFAULT_TIMEOUT);
    }
}
