package com.product.adapter.in.controller.register_review;

import com.product.application.port.in.RegisterReviewUseCase;
import com.product.application.service.register_review.RegisterReviewServiceResponse;
import com.product.domain.model.Account;
import com.product.infrastructure.resolver.LoginAccount;
import com.product.infrastructure.response.ApiResponse;
import com.product.infrastructure.validation.groups.ValidationSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterReviewController {

    private final RegisterReviewUseCase registerReviewUseCase;

    @PostMapping("/products/{productId}/reviews")
    ApiResponse<RegisterReviewResponse> registerReview(
        @Validated(ValidationSequence.class) @RequestBody RegisterReviewRequest request,
        @LoginAccount Account account,
        @PathVariable Long productId
    ) {
        RegisterReviewServiceResponse serviceResponse = registerReviewUseCase.registerReview(
            request.toCommand(productId, account));

        return ApiResponse.ok(RegisterReviewResponse.of(serviceResponse));
    }
}
