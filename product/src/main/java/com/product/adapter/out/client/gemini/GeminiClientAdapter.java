package com.product.adapter.out.client.gemini;

import com.product.application.port.out.GeminiClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class GeminiClientAdapter implements GeminiClientPort {

    private final GeminiClient client;

    @NewSpan
    @Override
    @CircuitBreaker(name = "gemini", fallbackMethod = "embeddingFallback")
    public float[] embedding(String document) {
        GeminiEmbeddingResponse result = client.embedding(GeminiEmbeddingRequest.of(document));
        return result.getResponse();
    }

    @NewSpan
    @Override
    @CircuitBreaker(name = "gemini", fallbackMethod = "queryFallback")
    public String query(String query) {
        GeminiQueryResponse result = client.query(GeminiQueryRequest.of(query));
        return result.getResponse();
    }

    private float[] embeddingFallback(String document, Throwable e) {
        log.error("[gemini] embeddingFallback : {}", e.getMessage());
        return null;
    }

    private String queryFallback(String query, Throwable e) {
        log.error("[gemini] queryFallback : {}", e.getMessage());
        return null;
    }
}
