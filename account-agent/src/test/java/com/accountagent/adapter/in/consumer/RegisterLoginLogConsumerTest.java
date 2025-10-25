package com.accountagent.adapter.in.consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.accountagent.fakeClass.FakeRegisterDqlInfoUseCase;
import com.accountagent.fakeClass.FakeRegisterLoginLogUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterLoginLogConsumerTest {

    FakeRegisterLoginLogUseCase fakeRegisterLoginLogUseCase;
    FakeRegisterDqlInfoUseCase fakeRegisterDqlInfoUseCase;
    RegisterLoginLogConsumer consumer;

    @BeforeEach
    void setUp() {
        fakeRegisterLoginLogUseCase = new FakeRegisterLoginLogUseCase();
        fakeRegisterDqlInfoUseCase = new FakeRegisterDqlInfoUseCase();
        consumer = new RegisterLoginLogConsumer(fakeRegisterDqlInfoUseCase, fakeRegisterLoginLogUseCase);
    }

    @Nested
    @DisplayName("[registerHistory] 로그인 로그를 등록하는 Consumer")
    class Describe_registerHistory {

        @Test
        @DisplayName("[success] Kafka 메시지를 받아 로그인 로그를 등록한다")
        void success(CapturedOutput output) {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when
            consumer.registerHistory(payload);

            // then
            assertThat(fakeRegisterLoginLogUseCase.receivedPayloads).hasSize(1);
            assertThat(fakeRegisterLoginLogUseCase.receivedPayloads.get(0)).contains("accountId");
            assertThat(output.toString()).contains("[login-log] <==");
            assertThat(output.toString()).contains("[login-log] result - ");
        }

        @Test
        @DisplayName("[error] UseCase에서 예외 발생 시 예외를 전파한다 (Kafka ErrorHandler가 DLQ로 전송)")
        void error_propagateException() {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            fakeRegisterLoginLogUseCase.shouldThrowException = true;

            // when & then
            assertThrows(RuntimeException.class, () -> {
                consumer.registerHistory(payload);
            });
        }
    }

    @Nested
    @DisplayName("[registerHistoryDlq] DLQ 메시지를 처리하는 Consumer")
    class Describe_registerHistoryDlq {

        @Test
        @DisplayName("[success] DLQ 메시지를 받아 재처리에 성공한다")
        void success(CapturedOutput output) {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when
            consumer.registerHistoryDlq(payload);

            // then
            assertThat(fakeRegisterLoginLogUseCase.receivedPayloads).hasSize(1);
            assertThat(fakeRegisterLoginLogUseCase.receivedPayloads.get(0)).contains("accountId");
            assertThat(output.toString()).contains("[login-log-dql] <==");
            assertThat(output.toString()).contains("[login-log-dql] result - ");
        }

        @Test
        @DisplayName("[error] DLQ 메시지 처리 실패 시 DLQ 로그를 저장한다")
        void error_saveDlqLog(CapturedOutput output) {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "email": "test@example.com",
                    "loginDateTime": "2025-10-25 10:00:00"
                }
                """;

            fakeRegisterLoginLogUseCase.shouldThrowException = true;

            // when
            consumer.registerHistoryDlq(payload);

            // then
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos).hasSize(1);
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos.get(0).topic).isEqualTo("account-login");
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos.get(0).payload).contains("accountId");
            assertThat(output.toString()).contains("[login-log-dlq] error -");
        }
    }
}
