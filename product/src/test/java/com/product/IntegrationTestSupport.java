package com.product;

import com.common.infrastructure.util.SnowflakeGenerator;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
public class IntegrationTestSupport {

    @Autowired
    protected SnowflakeGenerator snowflakeGenerator;

    @Autowired
    protected CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean(name = "shadedNettyGrpcServerLifecycle") // grpc mock 처리
    private Object grpcServerLifecycle;
}
