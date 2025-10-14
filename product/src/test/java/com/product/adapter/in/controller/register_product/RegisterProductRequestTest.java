package com.product.adapter.in.controller.register_product;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.domain.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterProductRequestTest {

    @Nested
    @DisplayName("[toCommand] 요청 객체를 커맨드 객체로 변환한다.")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 요청 객체를 커맨드 객체로 잘 변환하는지 확인한다.")
        void success() {

            // given
            RegisterProductRequest request = RegisterProductRequest.builder()
                .productName("상품명")
                .category("AUTOMOTIVE")
                .price(10000)
                .quantity(30)
                .productImgUrl("상품 이미지")
                .descriptionImgUrl("상품 설명")
                .build();

            // when
            RegisterProductCommand command = request.toCommand(Account.builder().build());

            // then
            assert command.productName().equals(request.productName());
            assert command.category().equals(request.category());
            assert command.price() == request.price();
            assert command.quantity() == request.quantity();
            assert command.productImgUrl().equals(request.productImgUrl());
            assert command.descriptionImgUrl().equals(request.descriptionImgUrl());
        }
    }
}