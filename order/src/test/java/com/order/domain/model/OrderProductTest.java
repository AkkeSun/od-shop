package com.order.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import grpc.product.ConfirmProductReservationResponse;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderProductTest {

    @Nested
    @DisplayName("[of] ConfirmProductReservationResponse로 OrderProduct를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] gRPC 응답으로 OrderProduct를 생성한다")
        void success() {
            // given
            ConfirmProductReservationResponse response = ConfirmProductReservationResponse.newBuilder()
                .setProductId(1L)
                .setSellerId(2L)
                .setBuyQuantity(3L)
                .build();

            // when
            OrderProduct orderProduct = OrderProduct.of(response);

            // then
            assertThat(orderProduct.getProductId()).isEqualTo(1L);
            assertThat(orderProduct.getSellerId()).isEqualTo(2L);
            assertThat(orderProduct.getBuyQuantity()).isEqualTo(3L);
            assertThat(orderProduct.getBuyStatus()).isEqualTo("ORDER");
        }
    }

    @Nested
    @DisplayName("[updateCustomerId] 고객 ID를 업데이트하는 메소드")
    class Describe_updateCustomerId {

        @Test
        @DisplayName("[success] 고객 ID를 업데이트한다")
        void success() {
            // given
            OrderProduct orderProduct = OrderProduct.builder()
                .productId(1L)
                .sellerId(2L)
                .buyQuantity(3L)
                .buyStatus("ORDER")
                .build();

            Long newCustomerId = 100L;

            // when
            orderProduct.updateCustomerId(newCustomerId);

            // then
            assertThat(orderProduct.getCustomerId()).isEqualTo(100L);
        }
    }

    @Nested
    @DisplayName("[cancel] 주문 상품을 취소하는 메소드")
    class Describe_cancel {

        @Test
        @DisplayName("[success] 주문 상품을 취소 상태로 변경한다")
        void success() {
            // given
            OrderProduct orderProduct = OrderProduct.builder()
                .productId(1L)
                .customerId(1L)
                .sellerId(2L)
                .buyQuantity(3L)
                .buyStatus("ORDER")
                .regDateTime(LocalDateTime.now())
                .build();

            LocalDateTime cancelTime = LocalDateTime.now().plusHours(1);

            // when
            orderProduct.cancel(cancelTime);

            // then
            assertThat(orderProduct.getBuyStatus()).isEqualTo("CANCEL");
            assertThat(orderProduct.getUpdateDateTime()).isEqualTo(cancelTime);
        }

        @Test
        @DisplayName("[success] 이미 취소된 상품도 취소 처리할 수 있다")
        void success_alreadyCanceled() {
            // given
            LocalDateTime firstCancelTime = LocalDateTime.now();
            OrderProduct orderProduct = OrderProduct.builder()
                .productId(1L)
                .customerId(1L)
                .sellerId(2L)
                .buyQuantity(3L)
                .buyStatus("CANCEL")
                .updateDateTime(firstCancelTime)
                .build();

            LocalDateTime secondCancelTime = firstCancelTime.plusHours(1);

            // when
            orderProduct.cancel(secondCancelTime);

            // then
            assertThat(orderProduct.getBuyStatus()).isEqualTo("CANCEL");
            assertThat(orderProduct.getUpdateDateTime()).isEqualTo(secondCancelTime);
        }
    }
}
