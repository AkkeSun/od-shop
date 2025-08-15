package com.order.infrastructure.aop;

import static com.order.infrastructure.util.JsonUtil.toObjectNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void controllerMethods() {
    }

    @Pointcut("@annotation(com.order.infrastructure.aop.ExceptionHandlerLog))")
    private void controllerAdviceMethods() {
    }

    @Around("controllerMethods()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        String httpMethod = request.getMethod();
        String path = request.getRequestURI();

        ObjectNode requestInfo = new ObjectMapper().createObjectNode();
        ObjectNode requestParam = new ObjectMapper().createObjectNode();
        ObjectNode requestBody = new ObjectMapper().createObjectNode();
        ObjectNode userInfo = new ObjectMapper().createObjectNode();

        // request parameter
        request.getParameterMap().forEach((key, value) -> {
            requestParam.put(key, String.join(",", value));
        });

        // request body
        for (Object arg : joinPoint.getArgs()) {
            if (arg == null) {
                continue;
            }
            if (arg.toString().contains("{")) {
                requestBody = toObjectNode(arg);
            }
        }

        requestInfo.put("param", requestParam);
        requestInfo.put("body", requestBody);
        requestInfo.put("userInfo", userInfo);

        log.info("[{} {}] request - {}", httpMethod, path, requestInfo);
        Object result = joinPoint.proceed();
        log.info("[{} {}] response - {}", httpMethod, path, result);

        return result;
    }

    @Around("controllerAdviceMethods()")
    public Object controllerAdviceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = getRequest();
        Object result = joinPoint.proceed();
        log.info("[{} {}] response - {}", request.getMethod(), request.getRequestURI(), result);
        return result;
    }

    // CAUTION) gRPC 는 HTTP/2 통신 이므로 HttpServletRequest 는 사용할 수 없음
    private HttpServletRequest getRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            return servletAttrs.getRequest();
        }
        return null;
    }
}
