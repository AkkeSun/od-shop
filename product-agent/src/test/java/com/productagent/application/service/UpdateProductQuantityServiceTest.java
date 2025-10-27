package com.productagent.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.productagent.domain.model.RefundProductMessage;
import com.productagent.fakeClass.FakeProductClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class UpdateProductQuantityServiceTest {

    UpdateProductQuantityService service;
    FakeProductClientPort fakeProductClientPort;

    UpdateProductQuantityServiceTest() {
        fakeProductClientPort = new FakeProductClientPort();
        service = new UpdateProductQuantityService(fakeProductClientPort);
    }

    @BeforeEach
    void setup() {
        fakeProductClientPort.updateQuantityRequests.clear();
        fakeProductClientPort.shouldThrowException = false;
    }

    @Nested
    @DisplayName("[updateQuantity] 상품 수량을 업데이트하는 메소드")
    class Describe_updateQuantity {

        @Test
        @DisplayName("[success] JSON 페이로드를 파싱하여 수량을 업데이트한다")
        void success(CapturedOutput output) {
            // given
            String payload = "{\"productId\": 100, \"quantity\": 5}";

            // when
            service.updateQuantity(payload);

            // then
            assertThat(fakeProductClientPort.updateQuantityRequests).hasSize(1);
            RefundProductMessage message = fakeProductClientPort.updateQuantityRequests.get(0);
            assertThat(message.productId()).isEqualTo(100L);
            assertThat(message.quantity()).isEqualTo(5L);
            assertThat(output.toString()).contains("FakeProductClientPort updated quantity");
        }

        @Test
        @DisplayName("[success] 여러 번 호출 시 모두 처리된다")
        void success_multipleCalls(CapturedOutput output) {
            // given
            String payload1 = "{\"productId\": 100, \"quantity\": 5}";
            String payload2 = "{\"productId\": 200, \"quantity\": 10}";
            String payload3 = "{\"productId\": 300, \"quantity\": 3}";

            // when
            service.updateQuantity(payload1);
            service.updateQuantity(payload2);
            service.updateQuantity(payload3);

            // then
            assertThat(fakeProductClientPort.updateQuantityRequests).hasSize(3);
            assertThat(fakeProductClientPort.updateQuantityRequests)
                .extracting(RefundProductMessage::productId)
                .containsExactly(100L, 200L, 300L);
        }

        @Test
        @DisplayName("[error] Client 장애 시 예외 전파")
        void error_clientFailure() {
            // given
            fakeProductClientPort.shouldThrowException = true;
            String payload = "{\"productId\": 100, \"quantity\": 5}";

            // when & then
            assertThrows(RuntimeException.class, () -> service.updateQuantity(payload));
        }

        @Test
        @DisplayName("[error] 잘못된 JSON 형식 시 예외 발생")
        void error_invalidJson() {
            // given
            String invalidPayload = "invalid json";

            // when & then
            assertThrows(Exception.class, () -> service.updateQuantity(invalidPayload));
        }
    }
}
