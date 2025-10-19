package com.product.adapter.in.controller.find_recommend_product;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class FindRecommendProductController {

    private final FindRecommendProductUseCase useCase;

    @GetMapping("/products/recommendations")
    ApiResponse<FindRecommendProductResponse> findRecommendProducts(
        @Validated(ValidationSequence.class) FindRecommendProductRequest request,
        @LoginAccount LoginAccountInfo loginInfo
    ) {
        FindRecommendProductServiceResponse serviceResponse = useCase
            .findRecommendProductList(request.toCommand(loginInfo));

        return ApiResponse.ok(FindRecommendProductResponse.of(serviceResponse));
    }
}
