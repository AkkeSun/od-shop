package com.order.applicatoin.service.exists_customer_order;

import static org.assertj.core.api.Assertions.assertThat;

import com.order.applicatoin.port.in.command.ExistsCustomerOrderCommand;
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
class ExistsCustomerOrderServiceTest {

    ExistsCustomerOrderService service;
    FakeOrderStoragePort fakeOrderStoragePort;

    ExistsCustomerOrderServiceTest() {
        fakeOrderStoragePort = new FakeOrderStoragePort();
        service = new ExistsCustomerOrderService(fakeOrderStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeOrderStoragePort.database.clear();
        fakeOrderStoragePort.id = 0L;
    }

    @Nested
    @DisplayName("[exists] 고객의 주문 존재 여부를 확인하는 메소드")
    class Describe_exists {

        @Test
        @DisplayName("[success] 고객의 주문이 존재하면 true를 반환한다")
        void success() {
            // given
            Long customerId = 1L;
            Long productId = 100L;

            Order order = Order.builder()
                .customerId(customerId)
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(productId)
                        .customerId(customerId)
                        .sellerId(2L)
                        .buyQuantity(2L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            fakeOrderStoragePort.register(order);

            ExistsCustomerOrderCommand command = ExistsCustomerOrderCommand.builder()
                .customerId(customerId)
                .productId(productId)
                .build();

            // when
            ExistsCustomerOrderServiceResponse response = service.exists(command);

            // then
            assertThat(response.exists()).isTrue();
        }

        @Test
        @DisplayName("[success] 고객의 주문이 없으면 false를 반환한다")
        void success_notExists() {
            // given
            Long customerId = 1L;
            Long productId = 100L;

            ExistsCustomerOrderCommand command = ExistsCustomerOrderCommand.builder()
                .customerId(customerId)
                .productId(productId)
                .build();

            // when
            ExistsCustomerOrderServiceResponse response = service.exists(command);

            // then
            assertThat(response.exists()).isFalse();
        }

        @Test
        @DisplayName("[success] 다른 고객의 주문은 체크하지 않는다")
        void success_differentCustomer() {
            // given
            Long customerId = 1L;
            Long otherCustomerId = 2L;
            Long productId = 100L;

            Order order = Order.builder()
                .customerId(otherCustomerId)
                .totalPrice(50000)
                .products(List.of(
                    OrderProduct.builder()
                        .productId(productId)
                        .customerId(otherCustomerId)
                        .sellerId(3L)
                        .buyQuantity(1L)
                        .buyStatus("ORDER")
                        .regDateTime(LocalDateTime.now())
                        .build()
                ))
                .regDateTime(LocalDateTime.now())
                .build();

            fakeOrderStoragePort.register(order);

            ExistsCustomerOrderCommand command = ExistsCustomerOrderCommand.builder()
                .customerId(customerId)
                .productId(productId)
                .build();

            // when
            ExistsCustomerOrderServiceResponse response = service.exists(command);

            // then
            assertThat(response.exists()).isFalse();
        }
    }
}
