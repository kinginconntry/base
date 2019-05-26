package com.needto.tokenizer.imp;

import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
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
 * @author Administrator
 */
@Component
public class HanLPTokenizer implements ITokenizer {


    public enum Mode {
        /**
         * 标准
         */
        ST,
        /**
         * nlp
         */
        NLP,
        /**
         * 索引
         */
        INDEX,
        /**
         * n-最短路径分词
         */
        N_SHORT_PATH,
        /**
         * 最短路径分词
         */
        SHORT_PATH,
        /**
         * 极速分词
         */
        FAST;
    }

    private static final Segment N_SHORT_SEGMENT = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
    private static final Segment DIJKSTRA_SEGMENT = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
    private static Set<String> standard(String text) {
        Set<String> result = new HashSet<>();

        StandardTokenizer.segment(text).forEach(term->result.add(term.word));
        return result;
    }
    private static Set<String> nlp(String text) {
        Set<String> result = new HashSet<>();
        NLPTokenizer.segment(text).forEach(term->result.add(term.word));
        return result;
    }
    private static Set<String> index(String text) {
        Set<String> result = new HashSet<>();
        IndexTokenizer.segment(text).forEach(term->result.add(term.word));
        return result;
    }
    private static Set<String> speed(String text) {
        Set<String> result = new HashSet<>();
        SpeedTokenizer.segment(text).forEach(term->result.add(term.word));
        return result;
    }
    private static Set<String> nShort(String text) {
        Set<String> result = new HashSet<>();
        N_SHORT_SEGMENT.seg(text).forEach(term->result.add(term.word));
        return result;
    }
    private static Set<String> shortest(String text) {
        Set<String> result = new HashSet<>();
        DIJKSTRA_SEGMENT.seg(text).forEach(term->result.add(term.word));
        return result;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        Mode modeEnum = Mode.valueOf(mode);
        Set<String> result = null;
        switch (modeEnum){
            case ST:
                result = standard(text);
                break ;
            case NLP:
                result = nlp(text);
                break;
            case INDEX:
                result = index(text);
                break;
            case N_SHORT_PATH:
                result = nShort(text);
                break;
            case SHORT_PATH:
                result = shortest(text);
                break;
            case FAST:
                result = speed(text);
                break;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        map.put(Mode.ST.name(), standard(text));
        map.put(Mode.NLP.name(), nlp(text));
        map.put(Mode.INDEX.name(), index(text));
        map.put(Mode.N_SHORT_PATH.name(), nShort(text));
        map.put(Mode.SHORT_PATH.name(), shortest(text));
        map.put(Mode.FAST.name(), speed(text));
        return map;
    }

    @Override
    public String getCode() {
        return Type.HANLP.name();
    }
}
