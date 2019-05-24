package com.needto.tokenizer.util;

import org.aspectj.util.FileUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Administrator
 */
public class TokenizerUtils {

    private static final Set<String> WORDS = new HashSet<>();

    /**
     * 默认分词停顿符
     */
    static {
        WORDS.add(",");
        WORDS.add(";");
        WORDS.add(" ");
        WORDS.add("，");
        WORDS.add("。");
        WORDS.add("；");
        WORDS.add("？");
        WORDS.add("！");
        WORDS.add("!");
    }

    /**
     * 默认通用停顿词
     * @return
     */
    public static Set<String> getDefaultPause(){
        return WORDS;
    }

    /**
     * 根据配置文件获取词源，不包含空格，回车，换行
     * @param classPath
     * @return
     * @throws IOException
     */
    public static Set<String> getDicConfig(String classPath) throws IOException {
        Set<String> words = new HashSet<>();
        if(StringUtils.isEmpty(classPath)){
            return words;
        }
        ClassPathResource classPathResource = new ClassPathResource(classPath);
        File file = classPathResource .getFile();
        if(!file.exists()){
            return words;
        }
        String content = FileUtil.readAsString(file);
        if(StringUtils.isEmpty(content)){
            return words;
        }
        List<String> tempList = Utils.getList(content, "\n");
        for(String temp : tempList){
            Utils.trim(temp);
            if(StringUtils.isEmpty(temp)){
                return words;
            }
            List<String> arr = Utils.getList(temp, ",");
            for(String arrTemp : arr){
                Utils.trim(arrTemp);
                words.add(arrTemp);
            }

        }
        return words;
    }
}
