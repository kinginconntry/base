package com.needto.tokenizer.imp;

import com.needto.common.entity.Dict;
import com.needto.common.utils.Assert;
import com.needto.tokenizer.data.ITokenizer;
import com.needto.tokenizer.data.Type;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 中文分词
 */
@Component
public class WordTokenizer implements ITokenizer {

    private static Set<String> seg(String text, SegmentationAlgorithm segmentationAlgorithm) {
        Set<String> result = new HashSet<>();
        for(Word word : WordSegmenter.segWithStopWords(text, segmentationAlgorithm)){
            result.add(word.getText());
        }
        return result;
    }

    @Override
    public Set<String> get(String text, String mode, Dict config) {
        Assert.validateNull(mode);
        SegmentationAlgorithm modeEnum = SegmentationAlgorithm.valueOf(mode);
        return seg(text, modeEnum);
    }

    @Override
    public Map<String, Set<String>> getMore(String text, Dict config) {
        Map<String, Set<String>> map = new HashMap<>();
        for(SegmentationAlgorithm segmentationAlgorithm : SegmentationAlgorithm.values()){
            map.put(segmentationAlgorithm.name(), seg(text, segmentationAlgorithm));
        }
        return map;
    }

    @Override
    public String getCode() {
        return Type.WORD.name();
    }
}
