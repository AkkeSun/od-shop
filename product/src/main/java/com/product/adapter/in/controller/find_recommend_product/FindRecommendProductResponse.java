package com.product.adapter.in.controller.find_recommend_product;

import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import com.product.domain.model.ProductRecommend;
import java.util.List;
import lombok.Builder;

@Builder
record FindRecommendProductResponse(
    List<ProductRecommend> personallyList,
    List<ProductRecommend> popularList,
    List<ProductRecommend> trendList
) {

    static FindRecommendProductResponse of(FindRecommendProductServiceResponse serviceResponse) {
        return FindRecommendProductResponse.builder()
            .personallyList(serviceResponse.personallyList())
            .popularList(serviceResponse.popularList())
            .trendList(serviceResponse.trendList())
            .build();
    }
}
