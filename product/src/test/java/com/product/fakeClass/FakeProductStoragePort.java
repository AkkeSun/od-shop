package com.product.fakeClass;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakeProductStoragePort implements ProductStoragePort {

    public List<Product> database = new ArrayList<>();

    @Override
    public void register(Product product) {
        database.add(product);
    }

    @Override
    public void deleteById(Long productId) {
        database.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .ifPresent(database::remove);
    }

    @Override
    public void softDeleteById(Long productId, LocalDateTime deleteAt) {
        database.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .ifPresent(database::remove);
    }

    @Override
    public Product findById(Long productId) {
        try {
            return database.stream()
                .filter(product -> product.getId().equals(productId))
                .findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }
}
