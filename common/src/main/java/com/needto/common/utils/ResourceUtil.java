package com.needto.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Administrator
 * resource资源加载工具类
 */
public class ResourceUtil {

    public static File loadFile(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        return resource.getFile();
    }

    public static <T> T loadObject(String path, Class<T> tClass) throws IOException {
        File file = loadFile(path);
        return JSON.parseObject(FileUtils.readFileToByteArray(file), tClass);
    }

    public static InputStream loadInput(String path) throws IOException {
        Resource resource = new ClassPathResource(path);
        File file = resource.getFile();
        return new FileInputStream(file);
    }
}
