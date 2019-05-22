package com.needto.tokenizer.imp;

import com.needto.common.entity.Dict;
import com.needto.common.utils.Assert;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 中文分词
 */
@Component
public class SmartcnTokenizer implements ITokenizer {

    public enum Mode {
        SMARTCN;
    }

    private static final SmartChineseAnalyzer SMART_CHINESE_ANALYZER = new SmartChineseAnalyzer();

    private static Set<String> segText(String text) {
        Set<String> result = new HashSet<>();
        try {
            TokenStream tokenStream = SMART_CHINESE_ANALYZER.tokenStream("text", new StringReader(text));
            tokenStream.reset();
            while (tokenStream.incrementToken()){
                CharTermAttribute charTermAttribute = tokenStream.getAttribute(CharTermAttribute.class);
                result.add(charTermAttribute.toString());
            }
            tokenStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        Mode modeEnum = Mode.valueOf(mode);
        Set<String> result = null;
        switch (modeEnum){
            case SMARTCN:
                result = segText(text);
                break ;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(Mode.SMARTCN.name(), segText(text));
        return map;
    }

    @Override
    public String getCode() {
        return Type.SMARTCN.name();
    }
}
