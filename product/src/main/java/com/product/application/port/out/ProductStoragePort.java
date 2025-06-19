package com.product.application.port.out;

import com.product.domain.model.Product;
import java.time.LocalDateTime;

public interface ProductStoragePort {

    void register(Product product);

    void deleteById(Long productId);

    void softDeleteById(Long productId, LocalDateTime deleteAt);

    Product findById(Long productId);
}
