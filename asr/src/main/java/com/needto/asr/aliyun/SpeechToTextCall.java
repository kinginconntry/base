package com.needto.asr.aliyun;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.idst.nls.event.NlsEvent;
import com.alibaba.idst.nls.event.NlsListener;
import com.alibaba.idst.nls.protocol.NlsResponse;
import org.springframework.util.StringUtils;

class SpeechToTextCall implements NlsListener {
    enum CallStatus {
        PENDING,
        SUCCESS,
        FAILED
    }

    private AsrResult result;
    private volatile CallStatus status = CallStatus.PENDING;

    SpeechToTextCall() {
    }

    public synchronized AsrResult getResult(){
        return this.result;
    }

    /**
     * 更新结果
     * @param status
     * @param data
     */
    private synchronized void updateResult(CallStatus status, AsrResult data) {
        //不能指定状态为初始或者空
        if (status == null) {
            throw new IllegalArgumentException("status");
        }
        if(this.status == CallStatus.PENDING){
            this.status = status;
            this.result = data;
        }
    }


    @Override
    public void onMessageReceived(NlsEvent e) {
        NlsResponse response = e.getResponse();
        int statusCode = response.getStatus_code();
        String asrRet = response.getAsr_ret();
        AsrResult<String> result = null;
        if (AsrStatusEnum.isSuccess(statusCode)) {
            result = AsrResult.forSuccess(statusCode, null);
            if (asrRet != null && !"".equals(asrRet)) {
                //识别结果
                JSONObject jsonObject = JSONObject.parseObject(asrRet);
                String voice = jsonObject.getString("result");
                String msg = AsrStatusEnum.getDesc(statusCode);
                result.setOut(voice);
                result.setMsg(msg);
                this.updateResult(CallStatus.SUCCESS, result);
            }
        }
    }

    @Override
    public void onOperationFailed(NlsEvent e) {
        int status = e.getResponse().getStatus_code();
        String msg = AsrStatusEnum.getDesc(status);
        msg = StringUtils.isEmpty(msg) ? e.getErrorMessage() : msg;
        this.updateResult(CallStatus.FAILED, AsrResult.forError(msg));
    }

    @Override
    public void onChannelClosed(NlsEvent e) {
        this.updateResult(CallStatus.FAILED, AsrResult.forError("无法解析语音"));
    }
}
