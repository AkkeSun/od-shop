package com.product.adapter.out.persistence.jpa.shard2;

import static com.product.infrastructure.exception.ErrorCode.DoesNotExist_PRODUCT_RESERVE_INFO;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.exception.CustomNotFoundException;
import com.product.infrastructure.exception.ErrorCode;
import java.time.LocalDateTime;
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
    private final ProductReserveHistoryShard2Repository reserveHistoryRepository;

    @Override
    public ProductReserveHistory createReservation(Product product,
        ProductReserveHistory reserveHistory) {
        productRepository.save(ProductShard2Entity.of(product));

        ProductReserveHistoryShard2Entity entity = reserveHistoryRepository.save(
            ProductReserveHistoryShard2Entity.of(reserveHistory));
        return entity.toDomain();
    }

    @Override
    public void confirmReservation(Product product, ProductReserveHistory reserveHistory) {
        ProductMetricShard2Entity entity = ProductMetricShard2Entity.of(product);
        productRepository.save(entity.getProduct());
        metricRepository.save(entity);
        reserveHistoryRepository.deleteById(reserveHistory.id());
    }

    @Override
    public void cancelReservation(Product product, ProductReserveHistory reserveHistory) {
        productRepository.save(ProductShard2Entity.of(product));
        reserveHistoryRepository.deleteById(reserveHistory.id());
    }

    @Override
    public ProductReserveHistory findReservationById(Long reserveId) {
        ProductReserveHistoryShard2Entity entity = reserveHistoryRepository.findById(reserveId)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_PRODUCT_RESERVE_INFO));
        return entity.toDomain();
    }

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
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        Optional<ProductShard2Entity> optional =
            deleteYn.equals("A") ? productRepository.findById(productId) :
                productRepository.findByIdAndDeleteYn(productId, deleteYn);

        ProductShard2Entity entity = optional.orElseThrow(
            () -> new CustomNotFoundException(ErrorCode.DoesNotExist_PRODUCT_INFO));
        return entity.toDomain();
    }
}
