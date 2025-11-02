package com.accountagent.adapter.in.consumer;

import com.accountagent.application.port.in.RegisterDqlInfoUseCase;
import com.accountagent.application.port.in.RegisterLoginLogUseCase;
import com.accountagent.application.service.register_login_log.RegisterLoginLogServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterLoginLogConsumer {

    private final RegisterDqlInfoUseCase registerDqlInfoUseCase;
    private final RegisterLoginLogUseCase registerLoginLogUseCase;

    @KafkaListener(
        topics = "account-login",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "1"
    )
    void registerHistory(@Payload String payload) {
        log.info("[login-log] <== {}", payload);
        RegisterLoginLogServiceResponse response = registerLoginLogUseCase
            .registerLoginLog(payload);
        log.info("[login-log] result - {}", response.result());
    }

    @KafkaListener(
        topics = "account-login-dlq",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "1"
    )
    void registerHistoryDlq(@Payload String payload) {
        try {
            log.info("[login-log-dql] <== " + payload);
            RegisterLoginLogServiceResponse response = registerLoginLogUseCase
                .registerLoginLog(payload);
            log.info("[login-log-dql] result - " + response.result());
        } catch (Exception e) {
            log.error("[login-log-dlq] error - ", e);
            registerDqlInfoUseCase.registerDqlInfo("account-login", payload);
        }
    }
}
