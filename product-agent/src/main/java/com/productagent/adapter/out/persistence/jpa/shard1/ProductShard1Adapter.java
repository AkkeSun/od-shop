package com.productagent.adapter.out.persistence.jpa.shard1;


import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import com.productagent.infrastructure.exception.CustomNotFoundException;
import com.productagent.infrastructure.exception.ErrorCode;
import java.util.List;
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
    public void registerMetrics(List<Product> products) {
        List<ProductMetricShard1Entity> entities = products.stream()
            .map(ProductMetricShard1Entity::of)
            .toList();
        metricRepository.saveAll(entities);
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
