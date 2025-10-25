package com.accountagent.application.service.register_history;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.accountagent.fakeClass.FakeLogStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterHistoryServiceTest {

    RegisterHistoryService service;
    FakeLogStoragePort fakeLogStoragePort;

    RegisterHistoryServiceTest() {
        fakeLogStoragePort = new FakeLogStoragePort();
        service = new RegisterHistoryService(fakeLogStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeLogStoragePort.clear();
    }

    @Nested
    @DisplayName("[registerHistory] 계정 이력을 등록하는 메소드")
    class Describe_registerHistory {

        @Test
        @DisplayName("[success] 유효한 payload로 계정 이력을 등록한다")
        void success() {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "type": "CREATE",
                    "detailInfo": "계정 생성",
                    "regDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when
            RegisterHistoryServiceResponse response = service.registerHistory(payload);

            // then
            assertThat(response.result()).isEqualTo("Y");
            assertThat(fakeLogStoragePort.accountHistoryDatabase).hasSize(1);
            assertThat(fakeLogStoragePort.accountHistoryDatabase.get(0).accountId()).isEqualTo(1L);
            assertThat(fakeLogStoragePort.accountHistoryDatabase.get(0).type()).isEqualTo("CREATE");
            assertThat(fakeLogStoragePort.accountHistoryDatabase.get(0).detailInfo()).isEqualTo("계정 생성");
        }

        @Test
        @DisplayName("[error] Storage 장애 시 예외 전파")
        void error_storageFailure() {
            // given
            fakeLogStoragePort.shouldThrowException = true;
            String payload = """
                {
                    "accountId": 1,
                    "type": "CREATE",
                    "detailInfo": "계정 생성",
                    "regDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when & then
            assertThrows(RuntimeException.class,
                () -> service.registerHistory(payload)
            );
        }

        @Test
        @DisplayName("[error] 잘못된 JSON payload로 예외 발생")
        void error_invalidJson() {
            // given
            String invalidPayload = "invalid json";

            // when & then
            assertThrows(Exception.class,
                () -> service.registerHistory(invalidPayload)
            );
        }
    }
}
