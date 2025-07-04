package com.product.fakeClass;

import com.product.application.port.out.GeminiClientPort;

public class DummyGeminiClientPort implements GeminiClientPort {

    @Override
    public float[] embedding(String document) {
        return new float[0];
    }

    @Override
    public String query(String query) {
        return "";
    }
}
