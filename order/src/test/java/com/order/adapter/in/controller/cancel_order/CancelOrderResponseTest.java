package com.order.adapter.in.controller.cancel_order;

import com.order.application.service.cancel_order.CancelOrderServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CancelOrderResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환하는지 확인한다")
        void success() {
            // given
            CancelOrderServiceResponse serviceResponse = CancelOrderServiceResponse.ofSuccess();

            // when
            CancelOrderResponse result = CancelOrderResponse.of(serviceResponse);

            // then
            assert (result.result());
        }
    }

}