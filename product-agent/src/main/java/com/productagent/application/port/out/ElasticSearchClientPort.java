package com.productagent.application.port.out;

import com.productagent.domain.model.Product;
import java.util.List;

public interface ElasticSearchClientPort {

    void register(Product product, float[] embedding);

    void deleteByIds(List<Long> productId);
}
