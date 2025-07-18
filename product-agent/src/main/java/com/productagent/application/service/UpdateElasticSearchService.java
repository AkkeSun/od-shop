package com.productagent.application.service;

import com.productagent.application.port.in.UpdateElasticSearchUseCase;
import com.productagent.application.port.out.ElasticSearchClientPort;
import com.productagent.application.port.out.GeminiClientPort;
import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateElasticSearchService implements UpdateElasticSearchUseCase {

    private final GeminiClientPort geminiClientPort;

    private final ProductStoragePort productStoragePort;

    private final ElasticSearchClientPort elasticSearchClientPort;

    @Override
    public void update() {

        int failedCount = 0;
        List<Product> products = productStoragePort.findByNeedsEsUpdate(true);
        for (Product product : products) {
            try {
                float[] embedding = geminiClientPort.embedding(product.getEmbeddingDocument());
                elasticSearchClientPort.register(product, embedding);
                product.updateNeedsEsUpdate(false);
            } catch (Exception e) {
                failedCount++;
                log.error("[updateElasticSearch] {} - {}", product.getId(), e.getMessage());
            }
        }

        productStoragePort.registerMetrics(products);
        log.info("[updateElasticSearch] success - {} / failed - {}", products.size() - failedCount,
            failedCount);
    }
}
