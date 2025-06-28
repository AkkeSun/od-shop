package com.product.application.port.out;

import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import java.time.LocalDate;
import java.util.List;

public interface RecommendStoragePort {

    List<ProductRecommend> findRecommendProductList(LocalDate checkDate, RecommendType type);
}
