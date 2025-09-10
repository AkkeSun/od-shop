package com.product.adapter.in.controller.find_recommend_product;

import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.service.find_recommend_product.FindRecommendProductServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
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
        @LoginAccount Account account
    ) {
        FindRecommendProductServiceResponse serviceResponse = useCase
            .findRecommendProductList(request.toCommand(account));

        return ApiResponse.ok(FindRecommendProductResponse.of(serviceResponse));
    }
}
