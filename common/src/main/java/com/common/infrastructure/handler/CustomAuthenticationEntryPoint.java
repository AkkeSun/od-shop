package com.common.infrastructure.handler;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.exception.ErrorCode;
import com.common.infrastructure.exception.ErrorResponse;
import com.common.infrastructure.response.ApiResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest req, HttpServletResponse res,
        AuthenticationException authException) throws IOException, ServletException {
        String responseBody = toJsonString(ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            ErrorResponse.builder()
                .errorCode(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getCode())
                .errorMessage(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY.getMessage())
                .build()));
        res.setContentType("application/json;charset=UTF-8");
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.getWriter().write(responseBody);
    }
}
