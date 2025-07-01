package com.product.application.port.out;

public interface GeminiClientPort {

    float[] embedding(String document);

    String query(String query);
}
