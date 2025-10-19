package com.order;

import com.common.infrastructure.aop.LogAspect;
import com.common.infrastructure.config.JasyptConfig;
import com.common.infrastructure.config.KafkaProducerConfig;
import com.common.infrastructure.exception.ExceptionAdvice;
import com.common.infrastructure.filter.ApiCallLogFilter;
import com.common.infrastructure.filter.JwtAuthenticationFilter;
import com.common.infrastructure.handler.CustomAccessDeniedHandler;
import com.common.infrastructure.handler.CustomAuthenticationEntryPoint;
import com.common.infrastructure.resolver.LoginAccountResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
    KafkaProducerConfig.class,
    LoginAccountResolver.class,
    LogAspect.class,
    JasyptConfig.class,
    ExceptionAdvice.class,
    JwtAuthenticationFilter.class,
    CustomAccessDeniedHandler.class,
    CustomAuthenticationEntryPoint.class,
    ApiCallLogFilter.class,
})
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

}
