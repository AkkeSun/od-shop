package com.product.adapter.in.update_product;

import com.product.application.port.in.command.UpdateProductCommand;
import com.product.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateProductRequestTest {

    @Nested
    @DisplayName("[toCommand] UpdateProductCommand 로 변환한다.")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] UpdateProductCommand 로 잘 변환하는지 확인한다.")
        void success() {
            // given
            UpdateProductRequest request = UpdateProductRequest.builder()
                .price(1000L)
                .productName("Test Product")
                .productImgUrl("http://example.com/image.jpg")
                .descriptionImgUrl("http://example.com/description.jpg")
                .build();
            Account account = Account.builder()
                .id(10L)
                .email("test@gmail.com")
                .build();
            Long productId = 1L;

            // when
            UpdateProductCommand command = request.toCommand(account, productId);

            // then
            assert command.account().id().equals(account.id());
            assert command.productId().equals(productId);
            assert command.productName().equals(request.productName());
            assert command.productImgUrl().equals(request.productImgUrl());
            assert command.descriptionImgUrl().equals(request.descriptionImgUrl());
            assert command.price() == request.price();
        }
    }
}