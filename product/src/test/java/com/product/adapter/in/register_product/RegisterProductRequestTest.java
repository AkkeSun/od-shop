package com.product.adapter.in.register_product;

import com.product.application.port.in.command.RegisterProductCommand;
import com.product.domain.model.Account;
import java.util.Set;
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
                .productOption(Set.of("옵션1", "옵션2"))
                .keywords(Set.of("키워드1", "키워드2"))
                .build();

            // when
            RegisterProductCommand command = request.toCommand(Account.builder().build());

            // then
            assert command.productName().equals(request.getProductName());
            assert command.category().equals(request.getCategory());
            assert command.price() == request.getPrice();
            assert command.quantity() == request.getQuantity();
            assert command.productImgUrl().equals(request.getProductImgUrl());
            assert command.descriptionImgUrl().equals(request.getDescriptionImgUrl());
            assert command.productOption().equals(request.getProductOption());
            assert command.keywords().equals(request.getKeywords());
        }
    }
}