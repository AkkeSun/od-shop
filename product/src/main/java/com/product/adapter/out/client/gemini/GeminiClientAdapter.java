package com.product.adapter.out.client.gemini;

import com.product.application.port.out.GeminiClientPort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Slf4j
@Component
class GeminiClientAdapter implements GeminiClientPort {

    private final GeminiClient client;

    GeminiClientAdapter(@Value("${service-constant.external.gemini.host}") String host,
        @Value("${service-constant.external.gemini.token}") String token) {

        RestClient restClient = RestClient.builder()
            .baseUrl(host)
            .defaultHeader("x-goog-api-key", token)
            .requestFactory(ClientHttpRequestFactories.get(
                ClientHttpRequestFactorySettings.DEFAULTS
                    .withConnectTimeout(Duration.ofSeconds(1))
                    .withReadTimeout(Duration.ofSeconds(5))))
            .build();

        this.client = HttpServiceProxyFactory.builder()
            .exchangeAdapter(RestClientAdapter.create(restClient))
            .build()
            .createClient(GeminiClient.class);
    }

    @Override
    @CircuitBreaker(name = "gemini", fallbackMethod = "embeddingFallback")
    public float[] embedding(String document) {
        GeminiEmbeddingResponse result = client.embedding(GeminiEmbeddingRequest.of(document));
        return result.getResponse();
    }

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
