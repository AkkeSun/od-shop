package com.order.application.service.cancel_order;

import static com.common.infrastructure.exception.ErrorCode.Business_ALREADY_CANCEL_ORDCER;
import static com.common.infrastructure.exception.ErrorCode.Business_NO_CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.order.application.port.in.command.CancelOrderCommand;
import com.order.domain.model.Order;
import com.order.domain.model.OrderProduct;
import com.order.fakeClass.DummyMessageProducerPort;
import com.order.fakeClass.FakeOrderStoragePort;
import com.common.infrastructure.exception.CustomBusinessException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CancelOrderServiceTest {

    private final CancelOrderService service;
    private final FakeOrderStoragePort orderStoragePort;
    private final DummyMessageProducerPort messageProducerPort;

    CancelOrderServiceTest() {
        orderStoragePort = new FakeOrderStoragePort();
        messageProducerPort = new DummyMessageProducerPort();
        service = new CancelOrderService(orderStoragePort, messageProducerPort);
    }

    @BeforeEach
    void setup() {
        orderStoragePort.database.clear();
    }

    @Nested
    @DisplayName("[cancel] 상품 구매를 취소하는 메소드")
    class Describe_cancel {

        @Test
        @DisplayName("[error] 구매를 취소하는 사람이 구매자가 아닌 경우 예외를 응답한다.")
        void error1() {
            // given
            Order order = orderStoragePort.register(Order.builder()
                .customerId(10L)
                .totalPrice(100)
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(20L)
                        .sellerId(30L)
                        .buyQuantity(20)
                        .buyStatus("ORDER")
                        .build()
                )).build());
            CancelOrderCommand command = CancelOrderCommand.builder()
                .orderId(order.orderNumber())
                .account(LoginAccountInfo.builder()
                    .id(100L)
                    .build())
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class,
                () -> service.cancel(command));

            // then
            assert result.getErrorCode().equals(Business_NO_CUSTOMER);
        }

        @Test
        @DisplayName("[error] 이미 취소된 상품인 경우 예외를 응답한다.")
        void error2() {
            // given
            Order order = orderStoragePort.register(Order.builder()
                .customerId(10L)
                .totalPrice(100)
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(20L)
                        .sellerId(30L)
                        .buyQuantity(20)
                        .buyStatus("CANCEL")
                        .build()
                )).build());
            CancelOrderCommand command = CancelOrderCommand.builder()
                .orderId(order.orderNumber())
                .account(LoginAccountInfo.builder()
                    .id(10L)
                    .build())
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class,
                () -> service.cancel(command));

            // then
            assert result.getErrorCode().equals(Business_ALREADY_CANCEL_ORDCER);
        }

        @Test
        @DisplayName("[success] 상품이 정상적으로 취소되는 경우 성공 메시지를 응답한다.")
        void success() {
            // given
            Order order = orderStoragePort.register(Order.builder()
                .customerId(10L)
                .totalPrice(100)
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .products(List.of(
                    OrderProduct.builder()
                        .productId(20L)
                        .sellerId(30L)
                        .buyQuantity(20)
                        .buyStatus("ORDER")
                        .build()
                )).build());
            CancelOrderCommand command = CancelOrderCommand.builder()
                .orderId(order.orderNumber())
                .account(LoginAccountInfo.builder()
                    .id(10L)
                    .build())
                .build();

            // when
            CancelOrderServiceResponse response = service.cancel(command);

            // then
            assert response.result();
        }
    }
}