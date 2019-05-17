package com.needto.asr.aliyun;

import com.needto.common.utils.Assert;
import com.needto.common.utils.FileUtils;
import it.sauronsoftware.jave.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2018/6/1 0001
 * 阿里云通用语音解析的格式为pcm和wav，上传文件内容需要进行格式转换，这里使用jave库进行格式转换
 * jave官方文档：http://www.sauronsoftware.it/projects/jave/manual.php
 */
public class AsrFormatTransferUtils {

    private final static String TEMP_DIR = "." + File.separator + "voice";

    /**
     * MP3 转wav的配置
     */
    private final static EncodingAttributes MA3_TO_WAV = new EncodingAttributes();

    static {
        createTempDir();

        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        MA3_TO_WAV.setFormat("wav");
        MA3_TO_WAV.setAudioAttributes(audio);
    }

    /**
     * 创建临时文件目录
     * @throws IOException
     */
    private static void createTempDir() {
        try {
            File dir = new File(TEMP_DIR);
            if (!dir.exists()) {
                FileUtils.mkDir(dir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * mp3格式转wav格式
     */
    public static InputStream mp3ToWav(InputStream in) throws EncoderException, IOException {

        File source = new File(TEMP_DIR, UUID.randomUUID().toString().replace("-", "") + ".mp3");
        Assert.validateCondition(!source.createNewFile());

        FileUtils.write(in, new FileOutputStream(source));

        Encoder encoder = new Encoder();
        MultimediaInfo m = encoder.getInfo(source);
        long duration = m.getDuration();
        Assert.validateCondition(duration > 60000, "", "语音太长，无法解析");

        File target = new File(TEMP_DIR, UUID.randomUUID().toString().replace("-", "") + ".wav");
        Assert.validateCondition(!target.createNewFile());
        // 转化成新的输入流
        encoder.encode(source, target, MA3_TO_WAV);

        // 删除临时文件夹
        if (source.exists()) {
            source.delete();
        }
        byte[] targetByte = FileCopyUtils.copyToByteArray(target);
        if (target.exists()) {
            target.delete();
        }
        return new ByteArrayInputStream(targetByte);
    }


    /**
     * 转换转发
     * @param in
     * @param filePath
     * @return
     * @throws Exception
     */
    public static InputStream transferWav(InputStream in, String filePath) throws Exception {
        int index = filePath.lastIndexOf(".");
        String fileType = index > -1 ? filePath.substring(index+1) : "";
        VoiceFormat format = VoiceFormat.getFormat(fileType);
        switch (format) {
            case MP3:
                return mp3ToWav(in);
            default:
                return in;
        }
    }

    /**
     * 语音格式
     */
    private enum VoiceFormat {
        MP3, NONE;
        public static VoiceFormat getFormat(String format) {
            for (VoiceFormat c : VoiceFormat.values()) {
                if (!StringUtils.isEmpty(format) && c.name().equals(format.toUpperCase())) {
                    return c;
                }
            }
            return NONE;
        }
    }
}
