package com.product.adapter.out.persistence.jpa;

import com.product.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.product.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.infrastructure.util.ShardKeyUtil;
import io.micrometer.tracing.annotation.NewSpan;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class ProductStorageAdapter implements ProductStoragePort {

    private final ProductShard1Adapter shard1Adapter;
    private final ProductShard2Adapter shard2Adapter;

    @NewSpan
    @Override
    public void register(Product product) {
        if (ShardKeyUtil.isShard1(product.getId())) {
            shard1Adapter.register(product);
        } else {
            shard2Adapter.register(product);
        }
    }

    @NewSpan
    @Override
    public void deleteById(Long productId) {
        if (ShardKeyUtil.isShard1(productId)) {
            shard1Adapter.deleteById(productId);
        } else {
            shard2Adapter.deleteById(productId);
        }
    }

    @NewSpan
    @Override
    public Product findById(Long productId) {
        return ShardKeyUtil.isShard1(productId) ? shard1Adapter.findById(productId)
            : shard2Adapter.findById(productId);
    }
}
