package com.product.adapter.out.persistence.elasticSearch;

import com.product.application.port.out.ProductEsStoragePort;
import com.product.domain.model.Product;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductEsStorageAdapter implements ProductEsStoragePort {

    private final ProductEsRepository repository;

    @NewSpan
    @Override
    public void register(Product product, float[] embedding) {
        repository.save(ProductEsDocument.of(product, embedding));
    }

    @NewSpan
    @Override
    public void deleteById(Long productId) {
        repository.deleteById(productId);
    }
}
