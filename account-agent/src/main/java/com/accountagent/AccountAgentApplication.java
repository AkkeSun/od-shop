package com.accountagent;

import com.common.infrastructure.config.JasyptConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Import(JasyptConfig.class)
@EnableScheduling
@SpringBootApplication
public class AccountAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountAgentApplication.class, args);
    }

}
