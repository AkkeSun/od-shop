package com.productagent.adapter.out.client.elasticsearch;


import com.productagent.application.port.out.ElasticSearchClientPort;
import com.productagent.domain.model.Product;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.Duration;
import java.util.List;
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
class ElasticSearchClientAdapter implements ElasticSearchClientPort {

    private final ElasticSearchClient client;

    ElasticSearchClientAdapter(
        @Value("${service-constant.external.elasticsearch.host}") String host
    ) {
        RestClient restClient = RestClient.builder()
            .baseUrl(host)
            .requestFactory(ClientHttpRequestFactories.get(
                ClientHttpRequestFactorySettings.DEFAULTS
                    .withConnectTimeout(Duration.ofSeconds(1))
                    .withReadTimeout(Duration.ofSeconds(5))))
            .build();

        this.client = HttpServiceProxyFactory.builder()
            .exchangeAdapter(RestClientAdapter.create(restClient))
            .build()
            .createClient(ElasticSearchClient.class);
        ;
    }

    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "registerFallback")
    public void register(Product product, float[] embedding) {
        client.register(RegisterProductEsRequest.of(product, embedding), product.getId());
    }

    @Override
    @CircuitBreaker(name = "elasticsearch", fallbackMethod = "deleteByIdFallback")
    public void deleteByIds(List<Long> productIds) {
        for (Long productId : productIds) {
            client.deleteById(productId);
        }
    }

    private void registerFallback(Product product, float[] embedding, Throwable e) {
        log.error("[elasticSearch] registerFallback : {}", e.getMessage());
        throw new RuntimeException(e.getMessage());
    }

    private void deleteByIdFallback(List<Long> productIds, Throwable e) {
        log.error("[elasticSearch] deleteByIds ({}) : {}", productIds, e.getMessage());
    }
}
