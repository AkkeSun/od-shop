package com.account.infrastructure.config;

import com.account.infrastructure.resolver.LoginAccountResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final LoginAccountResolver loginAccountResolver;

    public WebConfig(LoginAccountResolver loginAccountResolver) {
        this.loginAccountResolver = loginAccountResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAccountResolver);
    }
}

