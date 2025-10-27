package com.order.application.service.reserve_product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.order.application.port.in.command.ReserveProductCommand;
import com.order.application.port.in.command.ReserveProductCommand.ReserveProductCommandItem;
import com.order.fakeClass.DummyMessageProducerPort;
import com.order.fakeClass.FakeProductClientPort;
import com.order.fakeClass.TestPropertiesHelper;
import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.order.infrastructure.properties.KafkaTopicProperties;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(OutputCaptureExtension.class)
class ReserveProductServiceTest {

    ReserveProductService service;
    KafkaTopicProperties kafkaTopicProperties;
    FakeProductClientPort productClientPort;
    DummyMessageProducerPort messageProducerPort;

    ReserveProductServiceTest() {
        this.kafkaTopicProperties = TestPropertiesHelper.createKafkaProperties();
        this.messageProducerPort = new DummyMessageProducerPort();
        this.productClientPort = new FakeProductClientPort();
        this.service = new ReserveProductService(kafkaTopicProperties, productClientPort, messageProducerPort);
    }

    @BeforeEach
    void setup() {
        productClientPort.mapDatabase.clear();
    }

    @Nested
    @DisplayName("[reservation] 상품 구매를 예약하는 메소드")
    class Describe_reservation {

        @Test
        @DisplayName("[success] 예약에 성공하면 성공 메시지를 응답한다")
        void success() {
            // given
            ReserveProductCommand command = ReserveProductCommand.builder()
                .accountId(10L)
                .items(List.of(ReserveProductCommandItem.builder()
                    .productId(20L)
                    .quantity(10L)
                    .build()))
                .build();

            // when
            List<ReserveProductServiceResponse> results = service.reservation(command);

            // then
            assert results.size() == 1;
            ReserveProductServiceResponse result = results.getFirst();
            assert result.productId().equals(20L);
            assert result.reserveId().equals(15L);
        }

        @Test
        @DisplayName("[error] 예약에 실패하면 보상트랜젝션을 위한 메시지를 발송하고 에러를 응답한다")
        void error(CapturedOutput output) {
            // given
            ReserveProductCommand command = ReserveProductCommand.builder()
                .accountId(10L)
                .items(List.of(
                    ReserveProductCommandItem.builder()
                        .productId(20L)
                        .quantity(10L)
                        .build(),
                    ReserveProductCommandItem.builder()
                        .productId(99L)
                        .quantity(10L)
                        .build()))
                .build();

            // when
            CustomGrpcResponseError result = assertThrows(CustomGrpcResponseError.class,
                () -> service.reservation(command));

            // then
            assert output.toString().contains("DummyMessageProducerPort");
            assert result.getErrorMessage().equals("null - 99");
        }
    }
}