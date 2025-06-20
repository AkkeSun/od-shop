package com.product.adapter.in.controller.register_comment;

import com.product.application.service.register_comment.RegisterCommentServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterCommentResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환되는지 확인한다.")
        void success() {
            // given
            RegisterCommentServiceResponse serviceResponse =
                RegisterCommentServiceResponse.builder()
                    .result(true)
                    .build();

            // when
            RegisterCommentResponse response = RegisterCommentResponse.of(serviceResponse);

            // then
            assert response.result().equals(serviceResponse.result());
        }
    }
}