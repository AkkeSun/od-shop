package com.product.adapter.out.client.gemini;

import java.util.List;
import lombok.Builder;

@Builder
record GeminiContentRequest(
    List<GeminiContentText> parts
) {

    @Builder
    record GeminiContentText(
        String text
    ) {

    }

    static GeminiContentRequest of(String text) {
        return GeminiContentRequest.builder()
            .parts(List.of(GeminiContentText.builder()
                .text(text)
                .build()))
            .build();
    }

}
