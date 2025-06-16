package com.product.fakeClass;

import com.product.application.port.out.ProductEsStoragePort;
import com.product.domain.model.Product;
import java.util.ArrayList;
import java.util.List;

public class FakeProductEsStoragePort implements ProductEsStoragePort {

    public List<Product> database = new ArrayList<>();

    @Override
    public void register(Product product, float[] embedding) {
        if (product.getProductName().equals("error")) {
            throw new RuntimeException("Error registering product");
        }
        database.add(product);
    }
}
