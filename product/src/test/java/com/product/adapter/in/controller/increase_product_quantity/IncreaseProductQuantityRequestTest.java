package com.product.adapter.in.controller.increase_product_quantity;

import com.product.application.port.in.command.IncreaseProductQuantityCommand;
import com.product.domain.model.Account;
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
            Long productId = 1L;
            Account account = Account.builder()
                .id(1L)
                .build();
            IncreaseProductQuantityRequest request = IncreaseProductQuantityRequest.builder()
                .quantity(10)
                .build();

            // when
            IncreaseProductQuantityCommand command = request.of(productId, account);

            // then
            assert command.productId().equals(productId);
            assert command.quantity() == request.quantity();
            assert command.account().equals(account);
        }
    }
}