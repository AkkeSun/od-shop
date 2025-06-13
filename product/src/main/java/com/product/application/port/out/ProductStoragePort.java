package com.product.application.port.out;

import com.product.domain.model.Product;

public interface ProductStoragePort {

    void register(Product product);

    void deleteById(Long productId);
}
