package com.product.fakeClass;

import com.product.application.port.out.ProductStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductReserveHistory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FakeProductStoragePort implements ProductStoragePort {

    public List<Product> database = new ArrayList<>();

    @Override
    public ProductReserveHistory createReservation(Product product,
        ProductReserveHistory reserveHistory) {
        return null;
    }

    @Override
    public void confirmReservation(Product product, ProductReserveHistory reserveHistory) {

    }

    @Override
    public void cancelReservation(Product product, ProductReserveHistory reserveHistory) {

    }

    @Override
    public ProductReserveHistory findReservationById(Long reserveId) {
        return null;
    }

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
    public Product findByIdAndDeleteYn(Long productId, String deleteYn) {
        try {
            if (deleteYn.equals("A")) {
                return database.stream()
                    .filter(product -> product.getId().equals(productId))
                    .findFirst()
                    .orElseThrow();
            }
            return database.stream()
                .filter(product -> product.getId().equals(productId))
                .filter(product -> product.getDeleteYn().equals(deleteYn))
                .findFirst()
                .orElseThrow();
        } catch (Exception e) {
            return null;
        }
    }
}
