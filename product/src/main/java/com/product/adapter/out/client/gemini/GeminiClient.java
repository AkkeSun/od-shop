package com.product.adapter.out.client.gemini;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface GeminiClient {

    @PostExchange("/v1/models/text-embedding-004:embedContent")
    GeminiEmbeddingResponse embedding(@RequestBody GeminiEmbeddingRequest request);

    @PostExchange("/v1/models/gemini-2.0-flash:generateContent")
    GeminiQueryResponse query(@RequestBody GeminiQueryRequest request);
}
