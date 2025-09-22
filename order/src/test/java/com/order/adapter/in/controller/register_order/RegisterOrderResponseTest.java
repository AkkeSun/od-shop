package com.order.adapter.in.controller.register_order;

import com.order.applicatoin.service.register_order.RegisterOrderServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterOrderResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환하는지 확인한다")
        void success() {
            // given
            RegisterOrderServiceResponse serviceResponse = RegisterOrderServiceResponse.builder()
                .result(Boolean.TRUE)
                .orderNumber(10L)
                .build();

            // when
            RegisterOrderResponse result = RegisterOrderResponse.of(serviceResponse);

            // then
            assert result.result().equals(serviceResponse.result());
            assert result.orderNumber().equals(serviceResponse.orderNumber());
        }
    }
}