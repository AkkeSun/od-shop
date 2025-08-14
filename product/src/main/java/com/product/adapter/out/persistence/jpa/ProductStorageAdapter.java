package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import com.product.infrastructure.util.ShardKeyUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ProductStorageAdapter implements ProductStoragePort {

    private final ProductShard1Adapter shard1Adapter;
    private final ProductShard2Adapter shard2Adapter;

    @Override
    public ProductReserveHistory createReservation(Product product,
        ProductReserveHistory reserveHistory) {
        if (ShardKeyUtil.isShard1(product.getId())) {
            return shard1Adapter.createReservation(product, reserveHistory);
        }
        return shard2Adapter.createReservation(product, reserveHistory);
    }

    @Override
    public void confirmReservation(Product product, ProductReserveHistory reserveHistory) {
        if (ShardKeyUtil.isShard1(product.getId())) {
            shard1Adapter.confirmReservation(product, reserveHistory);
        } else {
            shard2Adapter.confirmReservation(product, reserveHistory);
        }
    }

    @Override
    public void cancelReservation(Product product, ProductReserveHistory reserveHistory) {
        if (ShardKeyUtil.isShard1(product.getId())) {
            shard1Adapter.cancelReservation(product, reserveHistory);
        } else {
            shard2Adapter.cancelReservation(product, reserveHistory);
        }
    }

    @Override
    public ProductReserveHistory findReservationById(Long reserveId) {
        if (ShardKeyUtil.isShard1(reserveId)) {
            return shard1Adapter.findReservationById(reserveId);
        }
        return shard2Adapter.findReservationById(reserveId);
    }

    @Override
    public void register(Product product) {
        if (ShardKeyUtil.isShard1(product.getId())) {
            shard1Adapter.register(product);
        } else {
            shard2Adapter.register(product);
        }
    }

    @Override
    public void deleteById(Long productId) {
        if (ShardKeyUtil.isShard1(productId)) {
            shard1Adapter.deleteById(productId);
        } else {
            shard2Adapter.deleteById(productId);
        }
    }

    @Override
    public void softDeleteById(Long productId, LocalDateTime deleteAt) {
        if (ShardKeyUtil.isShard1(productId)) {
            shard1Adapter.softDeleteById(productId, deleteAt);
        } else {
            shard2Adapter.softDeleteById(productId, deleteAt);
        }
    }

    @Override
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        return ShardKeyUtil.isShard1(productId) ?
            shard1Adapter.findByIdAndDeleteYn(productId, deleteYn) :
            shard2Adapter.findByIdAndDeleteYn(productId, deleteYn);
    }
}
