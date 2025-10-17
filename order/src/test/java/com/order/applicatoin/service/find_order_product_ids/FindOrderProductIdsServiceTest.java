package com.order.applicatoin.service.find_order_product_ids;

import static org.assertj.core.api.Assertions.assertThat;

import com.order.applicatoin.port.in.command.FindOrderProductIdsCommand;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.fakeClass.FakeOrderStoragePort;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class FindOrderProductIdsServiceTest {

    FindOrderProductIdsService service;
    FakeOrderStoragePort fakeOrderStoragePort;

    FindOrderProductIdsServiceTest() {
        fakeOrderStoragePort = new FakeOrderStoragePort();
        service = new FindOrderProductIdsService(fakeOrderStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeOrderStoragePort.database.clear();
        fakeOrderStoragePort.id = 0L;
    }

    @Nested
    @DisplayName("[findOrderProductIds] 고객이 주문한 상품 ID 목록을 조회하는 메소드")
    class Describe_findOrderProductIds {

        @Test
        @DisplayName("[success] 고객이 주문한 모든 상품 ID를 반환한다")
        void success() {
            // given
            Long customerId = 1L;

            Order order1 = Order.builder()
                .customerId(customerId)
                .totalPrice(50000)
                .receiverName("홍길동")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(100L)
                        .customerId(customerId)
                        .sellerId(2L)
                        .buyQuantity(1L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build(),
                    OrderProduct.builder()
                        .productId(101L)
                        .customerId(customerId)
                        .sellerId(2L)
                        .buyQuantity(2L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            Order order2 = Order.builder()
                .customerId(customerId)
                .totalPrice(30000)
                .receiverName("홍길동")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(102L)
                        .customerId(customerId)
                        .sellerId(3L)
                        .buyQuantity(1L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            fakeOrderStoragePort.register(order1);
            fakeOrderStoragePort.register(order2);

            FindOrderProductIdsCommand command = FindOrderProductIdsCommand.builder()
                .customerId(customerId)
                .limit(10)
                .build();

            // when
            List<Long> result = service.findOrderProductIds(command);

            // then
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrder(100L, 101L, 102L);
        }

        @Test
        @DisplayName("[success] 주문이 없는 고객은 빈 목록을 반환한다")
        void success_noOrders() {
            // given
            Long customerId = 1L;

            FindOrderProductIdsCommand command = FindOrderProductIdsCommand.builder()
                .customerId(customerId)
                .limit(10)
                .build();

            // when
            List<Long> result = service.findOrderProductIds(command);

            // then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("[success] 다른 고객의 주문 상품은 조회하지 않는다")
        void success_filterByCustomerId() {
            // given
            Long customerId = 1L;
            Long otherCustomerId = 2L;

            Order order1 = Order.builder()
                .customerId(customerId)
                .totalPrice(50000)
                .products(List.of(
                    OrderProduct.builder()
                        .productId(100L)
                        .customerId(customerId)
                        .sellerId(2L)
                        .buyQuantity(1L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            Order order2 = Order.builder()
                .customerId(otherCustomerId)
                .totalPrice(30000)
                .products(List.of(
                    OrderProduct.builder()
                        .productId(200L)
                        .customerId(otherCustomerId)
                        .sellerId(3L)
                        .buyQuantity(1L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            fakeOrderStoragePort.register(order1);
            fakeOrderStoragePort.register(order2);

            FindOrderProductIdsCommand command = FindOrderProductIdsCommand.builder()
                .customerId(customerId)
                .limit(10)
                .build();

            // when
            List<Long> result = service.findOrderProductIds(command);

            // then
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly(100L);
            assertThat(result).doesNotContain(200L);
        }
    }
}
