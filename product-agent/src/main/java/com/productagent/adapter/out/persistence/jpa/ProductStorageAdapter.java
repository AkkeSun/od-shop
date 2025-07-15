package com.productagent.adapter.out.persistence.jpa;

import static com.productagent.infrastructure.util.ShardKeyUtil.isShard1;

import com.productagent.adapter.out.persistence.jpa.shard1.ProductShard1Adapter;
import com.productagent.adapter.out.persistence.jpa.shard2.ProductShard2Adapter;
import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import java.util.ArrayList;
import java.util.List;
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
    public void registerMetrics(List<Product> products) {
        List<Product> shard1List = new ArrayList<>();
        List<Product> shard2List = new ArrayList<>();
        for (Product product : products) {
            if (isShard1(product.getId())) {
                shard1List.add(product);
            } else {
                shard2List.add(product);
            }
        }

        shard1Adapter.registerMetrics(shard1List);
        shard2Adapter.registerMetrics(shard2List);
    }

    @Override
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        return isShard1(productId) ? shard1Adapter.findByIdAndDeleteYn(productId, deleteYn) :
            shard2Adapter.findByIdAndDeleteYn(productId, deleteYn);
    }
}
