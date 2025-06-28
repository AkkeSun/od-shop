package com.product.adapter.out.client.elasticsearch;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ElasticSearchClientAdapter implements ElasticSearchClientPort {

    private final ElasticSearchClient client;

    @NewSpan
    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "registerFallback")
    public void register(Product product, float[] embedding) {
        client.register(RegisterProductEsRequest.of(product, embedding), product.getId());
    }

    @NewSpan
    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "deleteByIdFallback")
    public void deleteById(Long productId) {
        client.deleteById(productId);
    }

    @NewSpan
    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "findProductsFallback")
    public List<Product> findProducts(FindProductListCommand command) {
        FindProductsEsRequest request = FindProductsEsRequest.of(command);
        FindProductsEsResponse response = client.findProducts(toJsonString(request));
        return response.isEmpty() ? Collections.emptyList() : response.hits().hits().stream()
            .map(da -> da._source().toDomain()).toList();
    }

    @NewSpan
    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "findByEmbeddingFallback")
    public List<ProductRecommend> findByEmbedding(float[] embedding) {
        FindProductEsByEmbeddingRequest request = FindProductEsByEmbeddingRequest.of(embedding);
        FindProductsEsResponse response = client.findProducts(toJsonString(request));
        return response.isEmpty() ? Collections.emptyList() : response.hits().hits().stream()
            .map(da -> da._source().toRecommend()).toList();
    }

    private void registerFallback(Product product, float[] embedding, Throwable e) {
        log.error("[elasticSearch] registerFallback : {}", e.getMessage());
        throw new RuntimeException(e.getMessage());
    }

    private void deleteByIdFallback(Long productId, Throwable e) {
        log.error("[elasticSearch] deleteByIdFallback {{}) : {}", productId, e.getMessage());
    }

    private List<Product> findProductsFallback(FindProductListCommand command, Throwable e) {
        log.error("[elasticSearch] findProductsFallback : {}", e.getMessage());
        return new ArrayList<>();
    }
}
