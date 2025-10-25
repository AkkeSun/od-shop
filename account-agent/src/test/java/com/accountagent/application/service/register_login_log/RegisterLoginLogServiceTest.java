package com.accountagent.application.service.register_login_log;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.accountagent.fakeClass.FakeLogStoragePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterLoginLogServiceTest {

    RegisterLoginLogService service;
    FakeLogStoragePort fakeLogStoragePort;

    RegisterLoginLogServiceTest() {
        fakeLogStoragePort = new FakeLogStoragePort();
        service = new RegisterLoginLogService(fakeLogStoragePort);
    }

    @BeforeEach
    void setup() {
        fakeLogStoragePort.clear();
    }

    @Nested
    @DisplayName("[registerLoginLog] 로그인 로그를 등록하는 메소드")
    class Describe_registerLoginLog {

        @Test
        @DisplayName("[success] 유효한 payload로 로그인 로그를 등록한다")
        void success() {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when
            RegisterLoginLogServiceResponse response = service.registerLoginLog(payload);

            // then
            assertThat(response.result()).isEqualTo("Y");
            assertThat(fakeLogStoragePort.loginLogDatabase).hasSize(1);
            assertThat(fakeLogStoragePort.loginLogDatabase.get(0).accountId()).isEqualTo(1L);
            assertThat(fakeLogStoragePort.loginLogDatabase.get(0).email()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("[error] Storage 장애 시 예외 전파")
        void error_storageFailure() {
            // given
            fakeLogStoragePort.shouldThrowException = true;
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when & then
            assertThrows(RuntimeException.class,
                () -> service.registerLoginLog(payload)
            );
        }

        @Test
        @DisplayName("[error] 잘못된 JSON payload로 예외 발생")
        void error_invalidJson() {
            // given
            String invalidPayload = "invalid json";

            // when & then
            assertThrows(Exception.class,
                () -> service.registerLoginLog(invalidPayload)
            );
        }
    }
}
