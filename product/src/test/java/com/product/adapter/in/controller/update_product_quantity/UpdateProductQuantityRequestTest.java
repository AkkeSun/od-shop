package com.product.adapter.in.controller.update_product_quantity;

import com.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.domain.model.Account;
import com.product.domain.model.QuantityType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductQuantityRequestTest {

    @Nested
    @DisplayName("[of] UpdateProductQuantityCommand 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] UpdateProductQuantityCommand 를 생성하는 메소드")
        void of() {
            // given
            Long productId = 1L;
            Account account = Account.builder()
                .id(1L)
                .build();
            UpdateProductQuantityRequest request = UpdateProductQuantityRequest.builder()
                .quantity(10)
                .quantityType("ADD_QUANTITY")
                .build();

            // when
            UpdateProductQuantityCommand command = request.of(productId, account);

            // then
            assert command.productId().equals(productId);
            assert command.quantity() == request.quantity();
            assert command.type().equals(QuantityType.ADD_QUANTITY);
            assert command.account().equals(account);
        }
    }
}
