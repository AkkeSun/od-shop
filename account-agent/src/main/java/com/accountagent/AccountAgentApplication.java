package com.accountagent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AccountAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountAgentApplication.class, args);
    }

}
