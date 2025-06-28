package com.product.adapter.out.client.elasticsearch;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

public interface ElasticSearchClient {

    @PostExchange(url = "/product/_search", contentType = MediaType.APPLICATION_JSON_VALUE)
    FindProductsEsResponse findProducts(@RequestBody String request);

    @PutExchange("/product/_doc/{id}")
    void register(@RequestBody RegisterProductEsRequest request, @PathVariable Long id);

    @DeleteExchange("/product/_doc/{id}")
    void deleteById(@PathVariable Long id);
}
