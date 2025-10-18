package com.order.infrastructure.filter;

import static com.common.infrastructure.util.JsonUtil.toObjectNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.order.domain.model.Account;
import com.order.infrastructure.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomErrorLogFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        // response body
        String responseBody = new String(wrappedResponse.getContentAsByteArray(),
            StandardCharsets.UTF_8);

        if (responseBody.contains("BAD_REQUEST") ||
            (responseBody.contains("UNAUTHORIZED") && responseBody.contains("errorCode\":3003")) ||
            responseBody.contains("FORBIDDEN") && responseBody.contains("errorCode\":5002")) {

            String method = request.getMethod();
            String uri = request.getRequestURI();

            ObjectNode requestInfo = new ObjectMapper().createObjectNode();
            ObjectNode requestParam = new ObjectMapper().createObjectNode();
            ObjectNode requestBody = new ObjectMapper().createObjectNode();
            ObjectNode userInfo = new ObjectMapper().createObjectNode();

            // request parameter
            request.getParameterMap().forEach((key, value) -> {
                requestParam.put(key, String.join(",", value));
            });

            // request body
            try {
                String requestBodyStr = new String(wrappedRequest.getContentAsByteArray(),
                    StandardCharsets.UTF_8);
                requestBody = (ObjectNode) new ObjectMapper().readTree(requestBodyStr);
            } catch (Exception ignored) {
            }

            // user info
            String token = request.getHeader("Authorization");
            if (token != null) {
                try {
                    userInfo = toObjectNode(Account.of(jwtUtil.getClaims(token)));
                } catch (Exception ignore) {
                }
            }

            requestInfo.put("param", requestParam);
            requestInfo.put("body", requestBody);
            requestInfo.put("userInfo", userInfo);

            if (!request.getRequestURI().contains("/docs") && !uri.equals("/favicon.ico")) {
                log.info("[{} {}] request - {}", method, uri, requestInfo);
                log.info("[{} {}] response - {}", method, uri, responseBody);
            }
        }
        wrappedResponse.copyBodyToResponse();
    }
}
