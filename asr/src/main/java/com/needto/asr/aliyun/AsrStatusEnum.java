package com.needto.asr.aliyun;

/**
 * 阿里云语音返回状态码说明枚举类
 *
 * @author Administrator
 */

public enum AsrStatusEnum {
    SUCCESS(200, 1000, "成功处理"),
    FORMAT_ERROR(400, 4400, "错误请求"),
    REQUIRE_AUTHENTICATE(401, 4401, "请求要求身份验证"),
    AUTHENTICATE_FAILTURE(403, 4403, "鉴权失败"),
    EXCEED_CONCURRENCY(429, 4429, "请求并发太高"),
    TIME_OUT(408, 4408, "请求超时"),
    DEAL_ERROR(500, 4500, "处理错误"),
    NO_SERVICE(503, 4503, "服务不可用");

    private int status;
    private int closeFrame;
    private String desc;

    AsrStatusEnum() {
    }

    AsrStatusEnum(int status, int closeFrame, String desc) {
        this.status = status;
        this.closeFrame = closeFrame;
        this.desc = desc;
    }

    /**
     * 根据状态码获取状态描述
     *
     * @param status 状态码
     * @return
     */
    public static String getDesc(int status) {
        String desc = "";
        for (AsrStatusEnum asrStatusEnum : AsrStatusEnum.values()) {
            if (status == asrStatusEnum.status) {
                desc = asrStatusEnum.getDesc();
                break;
            }
        }
        return desc;
    }

    /**
     * 判断状态码是否成功，默认为200表示成功
     *
     * @param status 状态码
     * @return
     */
    public static boolean isSuccess(int status) {
        boolean success = false;
        if (status == AsrStatusEnum.SUCCESS.status) {
            success = true;
        }
        return success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCloseFrame() {
        return closeFrame;
    }

    public void setCloseFrame(int closeFrame) {
        this.closeFrame = closeFrame;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
