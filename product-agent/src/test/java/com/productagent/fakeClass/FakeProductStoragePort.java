package com.productagent.fakeClass;

import com.productagent.application.port.out.ProductStoragePort;
import com.productagent.domain.model.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeProductStoragePort implements ProductStoragePort {

    public Map<Long, Product> database = new HashMap<>();
    public boolean shouldThrowException = false;

    @Override
    public void registerMetrics(List<Product> products) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }

        for (Product product : products) {
            database.put(product.getId(), product);
        }
        log.info("FakeProductStoragePort registered {} products", products.size());
    }

    @Override
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        Product product = database.get(productId);
        if (product != null && product.getDeleteYn().equals(deleteYn)) {
            return product;
        }
        return null;
    }

    @Override
    public void deleteBySellerId(Long sellerId) {
        database.entrySet().removeIf(entry -> entry.getValue().getSellerId().equals(sellerId));
        log.info("FakeProductStoragePort deleted products by sellerId={}", sellerId);
    }

    @Override
    public List<Long> findIdBySellerId(Long sellerId) {
        return database.values().stream()
            .filter(product -> product.getSellerId().equals(sellerId))
            .map(Product::getId)
            .toList();
    }

    @Override
    public List<Product> findByNeedsEsUpdate(boolean needsEsUpdate) {
        return database.values().stream()
            .filter(product -> product.isNeedsEsUpdate() == needsEsUpdate)
            .toList();
    }
}
