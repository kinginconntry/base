package com.needto.asr.aliyun;


import org.springframework.util.StringUtils;

/**
 * @author Administrator
 * 文字转语音支持枚举
 * @date 2018/6/1 0001
 */
public enum VoiceFormat {
    WAV("WAV"), PCM("PCM");

    /**
     * 语音转换支持的格式：wav/pcm
     */
    private static String SUPPOERT_STRING = "";

    static {
        for (VoiceFormat c : VoiceFormat.values()) {
            SUPPOERT_STRING += c.name() + "/";
        }
        if (!StringUtils.isEmpty(SUPPOERT_STRING)) {
            SUPPOERT_STRING = SUPPOERT_STRING.substring(0, SUPPOERT_STRING.length() - 1);
        }
    }

    String format;

    VoiceFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * 检查格式是否支持
     * @param format 字符串
     * @return
     */
    public static boolean checkFormat(String format) {
        for (VoiceFormat c : VoiceFormat.values()) {
            if (!StringUtils.isEmpty(format) && c.name().equals(format.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查格式是否支持
     * @param format 枚举
     * @return
     */
    public static boolean checkFormat(VoiceFormat format) {
        if (format == null) {
            return false;
        }
        for (VoiceFormat c : VoiceFormat.values()) {
            if (c.name().equals(format.name())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回语音转换支持的格式
     * @return
     */
    public static String getSuppoertString() {
        return SUPPOERT_STRING;
    }
}
