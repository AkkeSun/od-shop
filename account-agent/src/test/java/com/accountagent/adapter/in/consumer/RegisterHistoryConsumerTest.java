package com.accountagent.adapter.in.consumer;

import static org.assertj.core.api.Assertions.assertThat;

import com.accountagent.fakeClass.FakeRegisterDqlInfoUseCase;
import com.accountagent.fakeClass.FakeRegisterHistoryUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class RegisterHistoryConsumerTest {

    FakeRegisterHistoryUseCase fakeRegisterHistoryUseCase;
    FakeRegisterDqlInfoUseCase fakeRegisterDqlInfoUseCase;
    RegisterHistoryConsumer consumer;

    @BeforeEach
    void setUp() {
        fakeRegisterHistoryUseCase = new FakeRegisterHistoryUseCase();
        fakeRegisterDqlInfoUseCase = new FakeRegisterDqlInfoUseCase();
        consumer = new RegisterHistoryConsumer(fakeRegisterHistoryUseCase, fakeRegisterDqlInfoUseCase);
    }

    @Nested
    @DisplayName("[registerHistory] 계정 이력을 등록하는 Consumer")
    class Describe_registerHistory {

        @Test
        @DisplayName("[success] Kafka 메시지를 받아 계정 이력을 등록한다")
        void success(CapturedOutput output) {
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
            consumer.registerHistory(payload);

            // then
            assertThat(fakeRegisterHistoryUseCase.receivedPayloads).hasSize(1);
            assertThat(fakeRegisterHistoryUseCase.receivedPayloads.get(0)).contains("accountId");
            assertThat(output.toString()).contains("[account-history] <==");
            assertThat(output.toString()).contains("[account-history] result - Y");
        }

        @Test
        @DisplayName("[error] UseCase에서 예외 발생 시 예외를 전파한다 (Kafka ErrorHandler가 DLQ로 전송)")
        void error_propagateException() {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "type": "CREATE",
                    "detailInfo": "계정 생성",
                    "regDateTime": "2025-10-25 10:00:00"
                }
                """;

            fakeRegisterHistoryUseCase.shouldThrowException = true;

            // when & then
            Assertions.assertThrows(RuntimeException.class, () -> {
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
                    "type": "CREATE",
                    "detailInfo": "계정 생성",
                    "regDateTime": "2025-10-25 10:00:00"
                }
                """;

            // when
            consumer.registerHistoryDlq(payload);

            // then
            assertThat(fakeRegisterHistoryUseCase.receivedPayloads).hasSize(1);
            assertThat(fakeRegisterHistoryUseCase.receivedPayloads.get(0)).contains("accountId");
            assertThat(output.toString()).contains("[account-history-dql] <==");
            assertThat(output.toString()).contains("[account-history-dql] result - Y");
        }

        @Test
        @DisplayName("[error] DLQ 메시지 처리 실패 시 DLQ 로그를 저장한다")
        void error_saveDlqLog(CapturedOutput output) {
            // given
            String payload = """
                {
                    "accountId": 1,
                    "type": "CREATE",
                    "detailInfo": "계정 생성",
                    "regDateTime": "2025-10-25 10:00:00"
                }
                """;

            fakeRegisterHistoryUseCase.shouldThrowException = true;

            // when
            consumer.registerHistoryDlq(payload);

            // then
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos).hasSize(1);
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos.get(0).topic).isEqualTo("account-history");
            assertThat(fakeRegisterDqlInfoUseCase.receivedDlqInfos.get(0).payload).contains("accountId");
            assertThat(output.toString()).contains("[account-history-dlq] error -");
        }
    }
}
