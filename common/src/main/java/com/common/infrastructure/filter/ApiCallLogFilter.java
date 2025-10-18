package com.common.infrastructure.filter;

import static com.common.infrastructure.util.JsonUtil.extractJsonField;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class ApiCallLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        // response body
        String responseBody = new String(wrappedResponse.getContentAsByteArray(),
            StandardCharsets.UTF_8);

        // -- Filter, Interceptor Level Exception Check ---
        String errorCode = extractJsonField(responseBody, "data", "errorCode");
        if (errorCode.endsWith("99")) {
            try {
                String method = request.getMethod();
                String uri = request.getRequestURI();

                ObjectNode requestInfo = new ObjectMapper().createObjectNode();
                ObjectNode requestParam = new ObjectMapper().createObjectNode();
                ObjectNode requestBody = new ObjectMapper().createObjectNode();

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

                requestInfo.put("param", requestParam);
                requestInfo.put("body", requestBody);

                // todo : 수정
                if (!uri.equals("/accounts/docs/account-api.yaml")) {
                    log.info("[{} {}] request - {}", method, uri, requestInfo);
                    log.info("[{} {}] response - {}", method, uri, responseBody);
                }

            } catch (Exception ignored) {
            }
        }
        wrappedResponse.copyBodyToResponse();
    }
}
