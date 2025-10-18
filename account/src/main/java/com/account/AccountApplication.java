package com.account;

import com.common.infrastructure.aop.LogAspect;
import com.common.infrastructure.config.JasyptConfig;
import com.common.infrastructure.config.KafkaProducerConfig;
import com.common.infrastructure.config.PasswordEncoderConfig;
import com.common.infrastructure.exception.ExceptionAdvice;
import com.common.infrastructure.filter.ApiCallLogFilter;
import com.common.infrastructure.filter.JwtAuthenticationFilter;
import com.common.infrastructure.resolver.LoginAccountResolver;
import com.common.infrastructure.util.UserAgentUtilImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({
    JasyptConfig.class,
    KafkaProducerConfig.class,
    LogAspect.class,
    ExceptionAdvice.class,
    JwtAuthenticationFilter.class,
    ApiCallLogFilter.class,
    UserAgentUtilImpl.class,
    LoginAccountResolver.class,
    PasswordEncoderConfig.class,
})
@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(AccountApplication.class, args);
    }
}
