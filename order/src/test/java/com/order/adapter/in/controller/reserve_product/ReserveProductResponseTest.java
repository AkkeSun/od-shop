package com.order.adapter.in.controller.reserve_product;

import com.order.applicatoin.service.reserve_product.ReserveProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReserveProductResponseTest {

    @Nested
    @DisplayName("[of] 서비스 응답을 API 응답으로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 서비스 응답을 API 응답으로 잘 변환하는지 확인한다")
        void success() {
            // given
            ReserveProductServiceResponse serviceResponse = ReserveProductServiceResponse.builder()
                .productId(10L)
                .reserveId(20L)
                .build();

            // when
            ReserveProductResponse result = ReserveProductResponse.of(serviceResponse);

            // then
            assert result.productId().equals(serviceResponse.productId());
            assert result.reserveId().equals(serviceResponse.reserveId());
        }
    }
}