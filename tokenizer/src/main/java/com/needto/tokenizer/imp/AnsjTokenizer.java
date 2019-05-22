package com.needto.tokenizer.imp;

import com.needto.common.entity.Dict;
import com.needto.common.utils.Assert;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 中文分词：
 * 1 比较智能（可以避免歧义）
 * 2 分割后凡事大于2个字的词都会被分割成单个字
 * 3 不可做数字智能分词
 * 4 自定义词库效果很差
 */
@Component
public class AnsjTokenizer implements ITokenizer {
    public enum Mode {
        /**
         * 基本分词，保证了最基本的分词，所涉及到的大约10万词，但查找速率快，准确率高
         */
        BASE_ANALYSIS,
        /**
         * 精准分词，它在易用性稳定性准确性上都是不错的平衡
         */
        TO_ANALYSIS,
        /**
         * nlp分词，具有用户自定义字典，数字识别，人名识别，地名识别，机构名识别，新词发现的功能，分词最为准确
         */
        NLP_ANALYSIS,
        /**
         * 面向索引分词，用于索引环节的中文分词
         */
        INDEX_ANALYSIS,
        /**
         * 自定义词典分词，自己定义所需的词典，当你在进行分词时候，会优先使用你自己定义的词进行分词。
         */
        DIC_ANALYSIS;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        Mode modeEnum = Mode.valueOf(mode);
        Set<String> result = null;
        switch (modeEnum){
            case BASE_ANALYSIS:
                result = new HashSet<>();
                for(Term term : BaseAnalysis.parse(text)){
                    result.add(term.getName());
                }
                break ;
            case TO_ANALYSIS:
                result = new HashSet<>();
                for(Term term : ToAnalysis.parse(text)){
                    result.add(term.getName());
                }
                break ;
            case NLP_ANALYSIS:
                result = new HashSet<>();
                for(Term term : NlpAnalysis.parse(text)){
                    result.add(term.getName());
                }
                break ;
            case INDEX_ANALYSIS:
                result = new HashSet<>();
                for(Term term : IndexAnalysis.parse(text)){
                    result.add(term.getName());
                }
                break ;
            case DIC_ANALYSIS:
                result = new HashSet<>();
                for(Term term : DicAnalysis.parse(text)){
                    result.add(term.getName());
                }
                break ;
            default:
                break;
        }
        return result;
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();

        Set<String> result = new HashSet<>();
        for(Term term : BaseAnalysis.parse(text)){
            result.add(term.getName());
        }
        map.put(AnsjTokenizer.Mode.BASE_ANALYSIS.name(), result);

        result = new HashSet<>();
        for(Term term : ToAnalysis.parse(text)){
            result.add(term.getName());
        }
        map.put(AnsjTokenizer.Mode.TO_ANALYSIS.name(), result);

        result = new HashSet<>();
        for(Term term : NlpAnalysis.parse(text)){
            result.add(term.getName());
        }
        map.put(AnsjTokenizer.Mode.NLP_ANALYSIS.name(), result);

        result = new HashSet<>();
        for(Term term : IndexAnalysis.parse(text)){
            result.add(term.getName());
        }
        map.put(AnsjTokenizer.Mode.INDEX_ANALYSIS.name(), result);

        result = new HashSet<>();
        for(Term term : DicAnalysis.parse(text)){
            result.add(term.getName());
        }
        map.put(Mode.DIC_ANALYSIS.name(), result);
        return map;
    }

    @Override
    public String getCode() {
        return Type.ANSJ.name();
    }
}
