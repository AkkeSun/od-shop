package com.product.fakeClass;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.domain.model.Product;
import java.util.ArrayList;
import java.util.List;

public class FakeElasticSearchClientPort implements ElasticSearchClientPort {

    public List<Product> database = new ArrayList<>();

    @Override
    public void register(Product product, float[] embedding) {
        if (product.getProductName().equals("error")) {
            throw new RuntimeException("Error registering product");
        }
        database.add(product);
    }

    @Override
    public void deleteById(Long productId) {
        database.removeIf(product -> product.getId().equals(productId));
    }

    @Override
    public List<Product> findProducts(FindProductListCommand command) {
        return List.of();
    }

}
