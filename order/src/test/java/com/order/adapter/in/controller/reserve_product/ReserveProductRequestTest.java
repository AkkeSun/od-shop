package com.order.adapter.in.controller.reserve_product;

import com.order.application.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReserveProductRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청 객체를 커멘드 객체로 변경하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 페이지를 입력하지 않았을 때 페이지를 0 으로 초기화하여 command 를 생성한다")
        void success() {
            // given
            ReserveProductRequest request = ReserveProductRequest.builder()
                .productId(10L)
                .quantity(20L)
                .build();

            // when
            ReserveProductCommandItem result = request.toCommandItem();

            // then
            assert result.productId().equals(request.productId());
            assert result.quantity().equals(request.quantity());
        }
    }
}