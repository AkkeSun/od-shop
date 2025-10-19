package com.product.adapter.in.controller.register_review;

import com.common.infrastructure.resolver.LoginAccount;
import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.response.ApiResponse;
import com.common.infrastructure.validation.groups.ValidationSequence;
import com.product.application.port.in.RegisterReviewUseCase;
import com.product.application.service.register_review.RegisterReviewServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterReviewController {

    private final RegisterReviewUseCase useCase;

    @PostMapping("/products/{productId}/reviews")
    ApiResponse<RegisterReviewResponse> registerReview(
        @Validated(ValidationSequence.class) @RequestBody RegisterReviewRequest request,
        @LoginAccount LoginAccountInfo loginInfo,
        @PathVariable Long productId
    ) {
        RegisterReviewServiceResponse serviceResponse = useCase.registerReview(
            request.toCommand(productId, loginInfo));

        return ApiResponse.ok(RegisterReviewResponse.of(serviceResponse));
    }
}
