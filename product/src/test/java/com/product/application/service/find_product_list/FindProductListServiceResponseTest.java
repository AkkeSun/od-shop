package com.product.application.service.find_product_list;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListServiceResponseTest {

    @Nested
    @DisplayName("[of] 상품 리스트와 커멘드로 ServiceResponse 를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 상품 리스트와 커멘드로 ServiceResponse 를 잘 생성하는지 확인한다.")
        void success() {
            // given
            Product product = Product.builder()
                .id(10L)
                .productName("product1")
                .sellerEmail("seller1")
                .productImgUrl("img1")
                .price(10000)
                .build();
            FindProductListCommand command = FindProductListCommand.builder()
                .page(0)
                .size(10)
                .build();

            // when
            FindProductListServiceResponse result = FindProductListServiceResponse.of(
                List.of(product), command);

            // then
            assert result.page() == command.page();
            assert result.size() == command.size();
            assert result.productList().getFirst().id().equals(product.getId());
            assert result.productList().getFirst().productName().equals(product.getProductName());
            assert result.productList().getFirst().sellerEmail().equals(product.getSellerEmail());
            assert result.productList().getFirst().productImgUrl()
                .equals(product.getProductImgUrl());
            assert result.productList().getFirst().price() == product.getPrice();

        }
    }
}