package com.product.adapter.out.persistence.jpa.shard2;

import static com.product.infrastructure.exception.ErrorCode.DoesNotExist_PROUCT_INFO;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomNotFoundException;
import java.time.LocalDateTime;
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

    @Override
    public void softDeleteById(Long productId, LocalDateTime deleteAt) {
        metricRepository.deleteById(productId);
        productRepository.softDeleteById(productId, deleteAt);
    }

    @Override
    public Product findById(Long productId) {
        ProductShard2Entity entity = productRepository.findByIdAndDeleteYn(productId, "N")
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_PROUCT_INFO));
        return entity.toDomain();
    }
}
