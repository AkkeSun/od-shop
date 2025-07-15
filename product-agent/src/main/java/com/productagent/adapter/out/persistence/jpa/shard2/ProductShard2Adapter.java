package com.productagent.adapter.out.persistence.jpa.shard2;


import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import com.productagent.infrastructure.exception.CustomNotFoundException;
import com.productagent.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    public void registerMetrics(List<Product> products) {
        List<ProductMetricShard2Entity> entities = products.stream()
            .map(ProductMetricShard2Entity::of)
            .toList();
        metricRepository.saveAll(entities);
    }


    @Override
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        Optional<ProductShard2Entity> optional =
            deleteYn.equals("A") ? productRepository.findById(productId) :
                productRepository.findByIdAndDeleteYn(productId, deleteYn);

        ProductShard2Entity entity = optional.orElseThrow(
            () -> new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO));
        return entity.toDomain();
    }

    @Override
    public void deleteBySellerId(Long sellerId) {
        productRepository.softDeleteByIdSellerId(sellerId, LocalDateTime.now());
        metricRepository.deleteAllById(productRepository.findIdBySellerId(sellerId));
    }

    @Override
    public List<Long> findIdBySellerId(Long sellerId) {
        return productRepository.findIdBySellerId(sellerId);
    }
}
