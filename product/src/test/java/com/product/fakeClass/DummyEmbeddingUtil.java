package com.product.fakeClass;

import com.product.infrastructure.util.EmbeddingUtil;

public class DummyEmbeddingUtil implements EmbeddingUtil {

    @Override
    public float[] embedToFloatArray(String document) {
        return new float[0];
    }
}
