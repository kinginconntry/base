package com.needto.tokenizer.imp;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import com.needto.tool.entity.Dict;
import com.needto.tool.utils.Assert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 中文分词
 */
@Component
public class JiebaTokenizer implements ITokenizer {
    public enum Mode {
        INDEX,
        SEARCH;
    }

    private static final JiebaSegmenter JIEBA_SEGMENTER = new JiebaSegmenter();

    private static Set<String> seg(String text, JiebaSegmenter.SegMode segMode) {
        Set<String> result = new HashSet<>();
        for(SegToken token : JIEBA_SEGMENTER.process(text, segMode)){
            result.add(token.word);
        }
        return result;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        Mode modeEnum = Mode.valueOf(mode);
        Set<String> result = null;
        switch (modeEnum){
            case INDEX:
                result = seg(text, JiebaSegmenter.SegMode.INDEX);
                break ;
            case SEARCH:
                result = seg(text, JiebaSegmenter.SegMode.SEARCH);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(Mode.INDEX.name(), seg(text, JiebaSegmenter.SegMode.INDEX));
        map.put(Mode.SEARCH.name(), seg(text, JiebaSegmenter.SegMode.SEARCH));
        return map;
    }

    @Override
    public String getCode() {
        return Type.JIEBA.name();
    }
}
