package com.order.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.order.applicatoin.port.in.command.RegisterOrderCommand;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Nested
    @DisplayName("[of] RegisterOrderCommand로 Order를 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 유효한 Command로 Order를 생성한다")
        void success() {
            // given
            RegisterOrderCommand command = RegisterOrderCommand.builder()
                .accountId(1L)
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구")
                .build();

            List<OrderProduct> products = List.of(
                OrderProduct.builder()
                    .productId(1L)
                    .customerId(1L)
                    .sellerId(2L)
                    .buyQuantity(2L)
                    .buyStatus("ORDER")
                    .regDateTime(LocalDateTime.now())
                    .build()
            );

            // when
            Order order = Order.of(command, products);

            // then
            assertThat(order.customerId()).isEqualTo(1L);
            assertThat(order.totalPrice()).isEqualTo(50000);
            assertThat(order.receiverName()).isEqualTo("홍길동");
            assertThat(order.receiverTel()).isEqualTo("010-1234-5678");
            assertThat(order.receiverAddress()).isEqualTo("서울시 강남구");
            assertThat(order.products()).hasSize(1);
            assertThat(order.regDateTime()).isNotNull();
        }
    }

    @Nested
    @DisplayName("[cancel] 주문을 취소하는 메소드")
    class Describe_cancel {

        @Test
        @DisplayName("[success] 주문에 포함된 모든 상품을 취소한다")
        void success() {
            // given
            LocalDateTime now = LocalDateTime.now();
            List<OrderProduct> products = new ArrayList<>(List.of(
                OrderProduct.builder()
                    .productId(1L)
                    .customerId(1L)
                    .sellerId(2L)
                    .buyQuantity(2L)
                    .buyStatus("ORDER")
                    .regDateTime(now)
                    .build(),
                OrderProduct.builder()
                    .productId(2L)
                    .customerId(1L)
                    .sellerId(3L)
                    .buyQuantity(1L)
                    .buyStatus("ORDER")
                    .regDateTime(now)
                    .build()
            ));

            Order order = Order.builder()
                .orderNumber(1L)
                .customerId(1L)
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구")
                .products(products)
                .regDateTime(now)
                .build();

            LocalDateTime cancelTime = now.plusHours(1);

            // when
            order.cancel(cancelTime);

            // then
            assertThat(order.products())
                .allMatch(product -> product.getBuyStatus().equals("CANCEL"));
            assertThat(order.products())
                .allMatch(product -> product.getUpdateDateTime().equals(cancelTime));
        }
    }

    @Nested
    @DisplayName("[isCustomer] 주문한 고객인지 확인하는 메소드")
    class Describe_isCustomer {

        @Test
        @DisplayName("[success] 주문한 고객이면 true를 반환한다")
        void success() {
            // given
            Order order = Order.builder()
                .customerId(1L)
                .totalPrice(50000)
                .build();

            Account account = Account.builder()
                .id(1L)
                .build();

            // when
            boolean result = order.isCustomer(account);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("[success] 주문한 고객이 아니면 false를 반환한다")
        void success_notCustomer() {
            // given
            Order order = Order.builder()
                .customerId(1L)
                .totalPrice(50000)
                .build();

            Account account = Account.builder()
                .id(2L)
                .build();

            // when
            boolean result = order.isCustomer(account);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("[isCanceled] 주문이 취소되었는지 확인하는 메소드")
    class Describe_isCanceled {

        @Test
        @DisplayName("[success] 취소된 상품이 있으면 true를 반환한다")
        void success() {
            // given
            List<OrderProduct> products = List.of(
                OrderProduct.builder()
                    .productId(1L)
                    .buyStatus("ORDER")
                    .build(),
                OrderProduct.builder()
                    .productId(2L)
                    .buyStatus("CANCEL")
                    .build()
            );

            Order order = Order.builder()
                .customerId(1L)
                .products(products)
                .build();

            // when
            boolean result = order.isCanceled();

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("[success] 취소된 상품이 없으면 false를 반환한다")
        void success_notCanceled() {
            // given
            List<OrderProduct> products = List.of(
                OrderProduct.builder()
                    .productId(1L)
                    .buyStatus("ORDER")
                    .build(),
                OrderProduct.builder()
                    .productId(2L)
                    .buyStatus("ORDER")
                    .build()
            );

            Order order = Order.builder()
                .customerId(1L)
                .products(products)
                .build();

            // when
            boolean result = order.isCanceled();

            // then
            assertThat(result).isFalse();
        }
    }
}
