package com.needto.asr.aliyun;

import com.needto.common.exception.LogicException;
import com.needto.common.utils.Assert;
import com.needto.common.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;

/**
 *
 * @author Administrator
 * @date 2018/5/31 0031
 */
@Service
public class AsrService {

    protected final static Logger LOG = LoggerFactory.getLogger(AsrService.class);


    @Autowired
    private SpeechToTextService speechToTextService;

    /**
     * 将语音连接转成语音文字
     * @param inputStream
     * @return
     */
    public String voiceToWord(InputStream inputStream, String filePath){
        Assert.validateNull(inputStream);
        try {
            InputStream temp = AsrFormatTransferUtils.transferWav(inputStream, filePath);
            AsrResult asrResult = speechToTextService.callAsrService(inputStream);
            Assert.validateCondition(!asrResult.isSuccess(), "", asrResult.getMsg());
            return Utils.nullToString(asrResult.getOut());
        } catch (Exception e) {
            LOG.error(e.toString());
            throw new LogicException("", "");
        }
    }
}
