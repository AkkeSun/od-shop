package com.product.adapter.in.controller.register_review;

import com.product.application.service.register_review.RegisterReviewServiceResponse;
import lombok.Builder;

@Builder
record RegisterReviewResponse(
    Boolean result
) {

    static RegisterReviewResponse of(RegisterReviewServiceResponse serviceResponse) {
        return RegisterReviewResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}
