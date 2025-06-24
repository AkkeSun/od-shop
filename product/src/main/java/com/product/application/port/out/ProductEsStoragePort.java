package com.product.application.port.out;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Product;
import java.util.List;

public interface ProductEsStoragePort {

    void register(Product product, float[] embedding);

    void deleteById(Long productId);

    List<Product> findByCategoryAndKeywords(FindProductListCommand command);
}
