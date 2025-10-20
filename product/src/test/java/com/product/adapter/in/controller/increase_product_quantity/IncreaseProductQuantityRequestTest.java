package com.product.adapter.in.controller.increase_product_quantity;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class IncreaseProductQuantityRequestTest {

    @Nested
    @DisplayName("[of] IncreaseProductQuantityCommand 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] IncreaseProductQuantityCommand 를 잘 생성하는지 확인한다")
        void of() {
            // given
            LoginAccountInfo loginInfo = LoginAccountInfo.builder()
                .id(1L)
                .build();
            IncreaseProductQuantityRequest request = IncreaseProductQuantityRequest.builder()
                .quantity(10)
                .build();

            // when
            IncreaseProductQuantityCommand command = request.of(loginInfo);

            // then
            assert command.quantity() == request.quantity();
            assert command.loginInfo().equals(loginInfo);
        }
    }
}