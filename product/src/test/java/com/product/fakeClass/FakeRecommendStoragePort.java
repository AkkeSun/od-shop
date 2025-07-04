package com.product.fakeClass;

import com.product.application.port.out.RecommendStoragePort;
import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FakeRecommendStoragePort implements RecommendStoragePort {

    public List<ProductRecommend> database = new ArrayList<>();

    @Override
    public List<ProductRecommend> findRecommendProductList(LocalDate checkDate,
        RecommendType type) {
        return database.stream()
            .filter(data -> data.type().equals(type))
            .toList();
    }
}
