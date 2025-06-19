package com.product.adapter.in.delete_product;

import com.product.application.service.delete_product.DeleteProductServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DeleteProductResponseTest {

    @Nested
    @DisplayName("[of] serviceResponse 를 ApiResponse로 변환한다.")
    class Describe_of {

        @Test
        @DisplayName("[success] serviceResponse 를 ApiResponse로 변환한다.")
        void success() {
            // given
            DeleteProductServiceResponse serviceResponse = DeleteProductServiceResponse.builder()
                .result(true)
                .build();

            // when
            DeleteProductResponse response = DeleteProductResponse.of(serviceResponse);

            // then
            assert response.result().equals(true);
        }
    }
}