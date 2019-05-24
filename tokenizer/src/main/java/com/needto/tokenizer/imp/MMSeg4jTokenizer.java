package com.needto.tokenizer.imp;

import com.chenlb.mmseg4j.*;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 中文分词
 */
@Component
public class MMSeg4jTokenizer implements ITokenizer {

    public enum Mode {
        SIMPLE,
        COMPLEX,
        MAX_WORD;
    }

    private static final Dictionary DIC = Dictionary.getInstance();
    private static final SimpleSeg SIMPLE_SEG = new SimpleSeg(DIC);
    private static final ComplexSeg COMPLEX_SEG = new ComplexSeg(DIC);
    private static final MaxWordSeg MAX_WORD_SEG = new MaxWordSeg(DIC);
    private Set<String> segText(String text, Seg seg) {
        Set<String> result = new HashSet<>();
        MMSeg mmSeg = new MMSeg(new StringReader(text), seg);
        try {
            Word word;
            while((word=mmSeg.next())!=null) {
                result.add(word.getString());
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
            case SIMPLE:
                result = segText(text, SIMPLE_SEG);
                break ;
            case COMPLEX:
                result = segText(text, COMPLEX_SEG);
                break;
            case MAX_WORD:
                result = segText(text, MAX_WORD_SEG);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(Mode.SIMPLE.name(), segText(text, SIMPLE_SEG));
        map.put(Mode.COMPLEX.name(), segText(text, COMPLEX_SEG));
        map.put(Mode.MAX_WORD.name(), segText(text, MAX_WORD_SEG));
        return map;
    }

    @Override
    public String getCode() {
        return Type.MMSEG4J.name();
    }
}
