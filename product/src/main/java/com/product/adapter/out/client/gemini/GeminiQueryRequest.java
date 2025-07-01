package com.product.adapter.out.client.gemini;

import lombok.Builder;

@Builder
public record GeminiQueryRequest(
    GeminiContentRequest contents
) {

    static GeminiQueryRequest of(String content) {
        return GeminiQueryRequest.builder()
            .contents(GeminiContentRequest.of(content))
            .build();
    }
}
