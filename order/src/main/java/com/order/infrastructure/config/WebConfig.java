package com.order.infrastructure.config;

import com.order.infrastructure.resolver.LoginAccountResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("!test")
public class WebConfig implements WebMvcConfigurer {

    private final LoginAccountResolver loginAccountResolver;

    public WebConfig(LoginAccountResolver loginAccountResolver) {
        this.loginAccountResolver = loginAccountResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/order/docs/**")
            .addResourceLocations("classpath:/static/docs/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAccountResolver);
    }
}
