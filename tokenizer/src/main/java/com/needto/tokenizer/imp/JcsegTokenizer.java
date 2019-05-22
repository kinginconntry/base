package com.needto.tokenizer.imp;

import com.needto.common.entity.Dict;
import com.needto.common.utils.Assert;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import org.lionsoul.jcseg.tokenizer.core.*;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * 中文分词：
 * 1 比较智能（简单分词不具备，其他分词模式具备）
 * 2 简单分割
 * 3 可做数字智能分词
 * 4 准确度最高
 * 5 可自定义词库
 */
@Component
public class JcsegTokenizer implements ITokenizer {
    public enum Mode {
        /**
         * 简单模式
         */
        SIMPLE,
        /**
         * 复杂模式
         */
        COMPLEX;
    }

    private static final JcsegTaskConfig CONFIG = new JcsegTaskConfig();
    private static final ADictionary DIC = DictionaryFactory.createDefaultDictionary(CONFIG);
    static {
        CONFIG.setLoadCJKSyn(false);
        CONFIG.setLoadCJKPinyin(false);
    }

    private Set<String> segText(String text, int segMode) {
        Set<String> result = new HashSet<>();
        try {
            ISegment seg = SegmentFactory.createJcseg(segMode, new StringReader(text), CONFIG, DIC);
            IWord word;
            while((word=seg.next())!=null) {
                result.add(word.getValue());
            }
        } catch (Exception ex) {
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
            case SIMPLE:
                result = segText(text, JcsegTaskConfig.SIMPLE_MODE);
                break ;
            case COMPLEX:
                result = segText(text, JcsegTaskConfig.COMPLEX_MODE);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();

        map.put(Mode.COMPLEX.name(), segText(text, JcsegTaskConfig.COMPLEX_MODE));
        map.put(Mode.SIMPLE.name(), segText(text, JcsegTaskConfig.SIMPLE_MODE));

        return map;
    }

    @Override
    public String getCode() {
        return Type.JCSEG.name();
    }
}
