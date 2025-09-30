package com.order.applicatoin.service.register_order;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.order.applicatoin.port.in.command.RegisterOrderCommand;
import com.order.applicatoin.port.in.command.RegisterOrderCommand.RegisterOrderCommandItem;
import com.order.domain.model.Order;
import com.order.fakeClass.DummyMessageProducerPort;
import com.order.fakeClass.FakeOrderStoragePort;
import com.order.fakeClass.FakeProductClientPort;
import com.order.infrastructure.exception.CustomGrpcResponseError;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterOrderServiceTest {

    RegisterOrderService service;
    FakeProductClientPort productClientPort;
    FakeOrderStoragePort orderStoragePort;
    DummyMessageProducerPort messageProducerPort;

    RegisterOrderServiceTest() {
        this.productClientPort = new FakeProductClientPort();
        this.orderStoragePort = new FakeOrderStoragePort();
        this.messageProducerPort = new DummyMessageProducerPort();
        service = new RegisterOrderService(productClientPort, orderStoragePort,
            messageProducerPort);
    }

    @Nested
    @DisplayName("[register] 주문을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 주문이 완료되면 성공 메시지를 응답한다.")
        void success() {
            // given
            RegisterOrderCommand command = RegisterOrderCommand.builder()
                .accountId(10L)
                .totalPrice(10000)
                .reserveInfos(List.of(RegisterOrderCommandItem.builder()
                    .productId(20L)
                    .reserveId(20L)
                    .build()))
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .receiverTel("receiverTel")
                .build();

            // when
            RegisterOrderServiceResponse result = service.register(command);

            // then
            assert result.result();
            assert orderStoragePort.database.size() == 1;
            Order order = orderStoragePort.database.getFirst();
            assert order.receiverName().equals("receiverName");
            assert order.receiverAddress().equals("receiverAddress");
            assert order.receiverTel().equals("receiverTel");
            assert order.totalPrice() == 10000;
            assert order.products().size() == 1;
            assert order.products().getFirst().getProductId() == 20L;
        }

        @Test
        @DisplayName("[error] 에약상품 처리중 오류 발생시 보상 트랜젝션을 위한 메시지를 전송하고 예외를 응답한다.")
        void error(CapturedOutput output) {
            // given
            RegisterOrderCommand command = RegisterOrderCommand.builder()
                .accountId(10L)
                .totalPrice(10000)
                .reserveInfos(List.of(
                    RegisterOrderCommandItem.builder()
                        .productId(20L)
                        .reserveId(20L)
                        .build(),
                    RegisterOrderCommandItem.builder()
                        .productId(99L)
                        .reserveId(99L)
                        .build())
                )
                .receiverName("receiverName")
                .receiverAddress("receiverAddress")
                .receiverTel("receiverTel")
                .build();

            // when
            CustomGrpcResponseError result = assertThrows(CustomGrpcResponseError.class,
                () -> service.register(command));

            // then
            assert output.toString().contains("DummyMessageProducerPort");
            assert result.getErrorMessage().equals("null - 99");
        }
    }
}