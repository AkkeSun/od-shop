package com.product.adapter.out.persistence.jpa.shard1;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional(transactionManager = "primaryTransactionManager")
public class ProductShard1Adapter implements ProductStoragePort {

    private final ProductShard1Repository productRepository;
    private final ProductMetricShard1Repository metricRepository;

    @Override
    public void register(Product product) {
        ProductMetricShard1Entity entity = ProductMetricShard1Entity.of(product);
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
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        Optional<ProductShard1Entity> optional =
            deleteYn.equals("A") ? productRepository.findById(productId) :
                productRepository.findByIdAndDeleteYn(productId, deleteYn);

        ProductShard1Entity entity = optional.orElseThrow(
            () -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
        return entity.toDomain();
    }
}
