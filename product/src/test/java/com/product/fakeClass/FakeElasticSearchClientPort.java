package com.product.fakeClass;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
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
        return database.stream()
            .filter(data -> data.getDeleteYn().equals("N"))
            .filter(data -> data.getCategory().equals(command.category()))
            .filter(data -> data.getProductName().contains(command.query()))
            .toList();
    }

    @Override
    public List<ProductRecommend> findByEmbedding(float[] embedding) {
        return database.stream()
            .map(product -> ProductRecommend.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .type(RecommendType.PERSONAL)
                .productImgUrl(product.getProductImgUrl())
                .sellerEmail(product.getSellerEmail())
                .price(product.getPrice())
                .build())
            .toList();
    }

}
