package com.product.application.port.out;

import com.product.domain.model.Product;

public interface ProductEsStoragePort {

    void register(Product product, float[] embedding);
}
