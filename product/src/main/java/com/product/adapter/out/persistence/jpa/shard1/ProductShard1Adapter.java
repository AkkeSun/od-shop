package com.product.adapter.out.persistence.jpa.shard1;

import static com.common.infrastructure.exception.ErrorCode.DoesNotExist_PRODUCT_INFO;
import static com.common.infrastructure.exception.ErrorCode.DoesNotExist_PRODUCT_RESERVE_INFO;

import com.common.infrastructure.exception.CustomNotFoundException;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
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
    private final ProductReserveHistoryShard1Repository reserveHistoryRepository;

    @Override
    public ProductReserveHistory createReservation(Product product,
        ProductReserveHistory reserveHistory) {
        productRepository.save(ProductShard1Entity.of(product));

        ProductReserveHistoryShard1Entity entity = reserveHistoryRepository.save(
            ProductReserveHistoryShard1Entity.of(reserveHistory));
        return entity.toDomain();
    }

    @Override
    public void confirmReservation(Product product, ProductReserveHistory reserveHistory) {
        ProductMetricShard1Entity entity = ProductMetricShard1Entity.of(product);
        productRepository.save(entity.getProduct());
        metricRepository.save(entity);
        reserveHistoryRepository.deleteById(reserveHistory.id());
    }

    @Override
    public void cancelReservation(Product product, ProductReserveHistory reserveHistory) {
        productRepository.save(ProductShard1Entity.of(product));
        reserveHistoryRepository.deleteById(reserveHistory.id());
    }

    @Override
    public ProductReserveHistory findReservationById(Long reserveId) {
        ProductReserveHistoryShard1Entity entity = reserveHistoryRepository.findById(reserveId)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_PRODUCT_RESERVE_INFO));
        return entity.toDomain();
    }

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
            () -> new CustomNotFoundException(DoesNotExist_PRODUCT_INFO));
        return entity.toDomain();
    }
}
