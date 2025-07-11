package com.product.adapter.in.controller.register_review;

import com.product.application.service.register_review.RegisterReviewServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterReviewResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환되는지 확인한다.")
        void success() {
            // given
            RegisterReviewServiceResponse serviceResponse =
                RegisterReviewServiceResponse.builder()
                    .result(true)
                    .build();

            // when
            RegisterReviewResponse response = RegisterReviewResponse.of(serviceResponse);

            // then
            assert response.result().equals(serviceResponse.result());
        }
    }
}