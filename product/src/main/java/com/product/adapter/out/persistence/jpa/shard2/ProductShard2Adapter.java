package com.product.adapter.out.persistence.jpa.shard2;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "secondaryTransactionManager")
public class ProductShard2Adapter implements ProductStoragePort {

    private final ProductShard2Repository productRepository;
    private final ProductMetricShard2Repository metricRepository;

    @Override
    public void register(Product product) {
        ProductMetricShard2Entity entity = ProductMetricShard2Entity.of(product);
        productRepository.save(entity.getProduct());
        metricRepository.save(entity);
    }

    @Override
    public void deleteById(Long productId) {
        metricRepository.deleteById(productId);
        productRepository.deleteById(productId);
    }
}
