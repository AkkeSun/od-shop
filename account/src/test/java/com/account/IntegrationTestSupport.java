package com.account;

import com.account.infrastructure.util.AesUtil;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
@EmbeddedKafka(partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:9092",
    },
    ports = {9092}
)
public class IntegrationTestSupport {

    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @Autowired
    protected AesUtil aesUtil;
}
