package com.product.application.port.out;

import com.product.domain.model.ProductMetric;

public interface ProductMetricStoragePort {

    ProductMetric register(ProductMetric productMetric);

    void deleteByProductId(Long productId);
}
