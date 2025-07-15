package com.productagent.application.port.out;

import com.productagent.domain.model.Product;
import java.util.List;

public interface ProductStoragePort {

    void registerMetrics(List<Product> products);

    Product findByIdAndDeleteYn(Long productId, String deleteYn);

    void deleteBySellerId(Long sellerId);

    List<Long> findIdBySellerId(Long sellerId);
}
