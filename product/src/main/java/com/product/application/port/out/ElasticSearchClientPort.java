package com.product.application.port.out;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import java.util.List;

public interface ElasticSearchClientPort {

    void register(Product product, float[] embedding);

    void deleteById(Long productId);

    List<Product> findProducts(FindProductListCommand command);

    List<ProductRecommend> findByEmbedding(float[] embedding);
}
