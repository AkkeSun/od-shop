package com.product.application.port.in;

import com.product.application.port.in.command.FindRecommendProductCommand;
import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;

public interface FindRecommendProductUseCase {

    FindRecommendProductServiceResponse findRecommendProductList(
        FindRecommendProductCommand command);
}
