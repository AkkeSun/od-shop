package com.productagent;

import com.common.infrastructure.config.JasyptConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(JasyptConfig.class)
@EnableScheduling
@SpringBootApplication
public class ProductAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductAgentApplication.class, args);
    }
}
