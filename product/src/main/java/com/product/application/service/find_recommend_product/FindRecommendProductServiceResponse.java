package com.product.application.service.find_recommend_product;

import com.product.domain.model.ProductRecommend;
import java.util.List;
import lombok.Builder;

@Builder
public record FindRecommendProductServiceResponse(
    List<ProductRecommend> personallyList,
    List<ProductRecommend> popularList,
    List<ProductRecommend> trendList
) {

}
