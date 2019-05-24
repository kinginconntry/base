package com.needto.tokenizer.imp;

import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import com.needto.tokenizer.entity.IkConfig;
import com.needto.tokenizer.util.TokenizerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

/**
 * 中文分词器：
 * 1 非智能分词器（不能避免歧义）
 * 2 保证尽可能保留二元词
 * 3 不可做数字智能分词
 * 4 可自定义词库
 *
 *
 */
@Component
public class IKTokenizer implements ITokenizer {

    public enum Mode {
        /**
         * 智能切分
         */
        IT,
        /**
         * 细粒度切分
         */
        LITTLE;
    }

    private static final Dict DEFAULT_CONFIG = new Dict();

    public static final String STOP_DICT = "stop";

    public static final String EXT_DICT = "ext";

    @Autowired
    private Environment environment;

    /**
     * 系统自带停顿词
     */
    private List<String> stopWords;

    /**
     * 扩展词库
     */
    private List<String> extWords;

    @PostConstruct
    public void init() throws IOException {
        /**
         * 初始化停顿词
         */
        String stopPath = environment.getProperty("ik.stop", "ikstopword.dic");
        Set<String> stop = TokenizerUtils.getDicConfig(stopPath);
        stopWords = new ArrayList<>(stop);

        /**
         * 扩展词库
         */
        String extPath = environment.getProperty("ik.ext", "ikextword.dic");
        Set<String> ext = TokenizerUtils.getDicConfig(extPath);
        extWords = new ArrayList<>(ext);
    }

    private Set<String> segText(String text, boolean useSmart, Dict config) {
        Dict temp = config;
        if(temp == null){
            temp = DEFAULT_CONFIG;
        }
        List<String> stop = temp.getValue(STOP_DICT, stopWords);
        List<String> ext = temp.getValue(EXT_DICT, extWords);
        Set<String> result = new HashSet<>();
        IkConfig cfg = new IkConfig();
        cfg.setUseSmart(useSmart);
        cfg.setExe(ext);
        cfg.setStop(stop);
        IKSegmenter ik = new IKSegmenter(new StringReader(text), useSmart);

        try {
            Lexeme word;
            while ((word = ik.next()) != null) {
                result.add(word.getLexemeText());
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        Mode modeEnum = Mode.valueOf(mode);
        Set<String> result = null;
        switch (modeEnum){
            case IT:
                result = segText(text, true, config);
                break ;
            case LITTLE:
                result = segText(text, false, config);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(IKTokenizer.Mode.IT.name(), segText(text, true, config));
        map.put(IKTokenizer.Mode.LITTLE.name(), segText(text, false, config));
        return map;
    }

    @Override
    public String getCode() {
        return Type.IK.name();
    }
}
