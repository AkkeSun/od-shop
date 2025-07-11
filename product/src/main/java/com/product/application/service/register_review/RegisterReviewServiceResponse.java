package com.product.application.service.register_review;

import lombok.Builder;

@Builder
public record RegisterReviewServiceResponse(
    boolean result
) {

    public static RegisterReviewServiceResponse ofSuccess() {
        return RegisterReviewServiceResponse.builder()
            .result(true)
            .build();
    }
}
