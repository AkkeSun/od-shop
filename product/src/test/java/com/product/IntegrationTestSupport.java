package com.product;

import com.product.infrastructure.util.EmbeddingUtil;
import com.product.infrastructure.util.SnowflakeGenerator;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(OutputCaptureExtension.class)
public class IntegrationTestSupport {

    @Autowired
    protected SnowflakeGenerator snowflakeGenerator;

    @Autowired
    protected EmbeddingUtil embeddingUtil;
}
