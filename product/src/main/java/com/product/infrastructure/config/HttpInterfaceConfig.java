package com.product.infrastructure.config;

import com.product.adapter.out.client.elasticsearch.ElasticSearchClient;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class HttpInterfaceConfig {

    @Value("${service-constant.external.elasticsearch}")
    private String elasticsearchHost;

    private final ClientHttpRequestFactorySettings defaultSettings;

    HttpInterfaceConfig() {
        this.defaultSettings = ClientHttpRequestFactorySettings.DEFAULTS
            .withConnectTimeout(Duration.ofSeconds(1))
            .withReadTimeout(Duration.ofSeconds(5));
    }

    @Bean
    RestClient webClient() {
        return RestClient.builder().build();
    }

    @Bean
    ElasticSearchClient elasticSearchClient() {
        RestClient restClient = RestClient.builder()
            .baseUrl(elasticsearchHost)
            .requestFactory(ClientHttpRequestFactories.get(defaultSettings))
            .build();

        return HttpServiceProxyFactory.builder()
            .exchangeAdapter(RestClientAdapter.create(restClient))
            .build()
            .createClient(ElasticSearchClient.class);
    }
}
