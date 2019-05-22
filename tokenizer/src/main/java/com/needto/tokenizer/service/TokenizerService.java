package com.needto.tokenizer.service;

import com.needto.common.service.ThingContainerService;
import com.needto.tokenizer.data.ITokenizer;

public class TokenizerService extends ThingContainerService<ITokenizer> {
    @Override
    protected Class<ITokenizer> getThingClass() {
        return ITokenizer.class;
    }
}
