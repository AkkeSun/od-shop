package com.account.infrastructure.aop;

import static com.account.infrastructure.util.JsonUtil.toObjectNode;

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

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final HttpServletRequest request;

    @Pointcut("@annotation(io.micrometer.tracing.annotation.NewSpan))")
    private void newSpan() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void controllerMethods() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ExceptionHandler))")
    private void controllerAdviceMethods() {
    }

    @Around("controllerMethods()")
    public Object controllerLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String httpMethod = request.getMethod();
        String path = request.getRequestURI();

        ObjectNode requestInfo = new ObjectMapper().createObjectNode();
        ObjectNode requestParam = new ObjectMapper().createObjectNode();
        ObjectNode requestBody = new ObjectMapper().createObjectNode();

        // request parameter
        request.getParameterMap().forEach((key, value) -> {
            requestParam.put(key, String.join(",", value));
        });

        // request body
        for (Object arg : joinPoint.getArgs()) {
            if (arg.toString().contains("{")) {
                requestBody = toObjectNode(arg);
            }
        }

        requestInfo.put("param", requestParam);
        requestInfo.put("body", requestBody);

        log.info("[{} {}] request - {}", httpMethod, path, requestInfo);
        Object result = joinPoint.proceed();
        log.info("[{} {}] response - {}", httpMethod, path, result);

        return result;
    }

    @Around("controllerAdviceMethods()")
    public Object controllerAdvieLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        log.info("[{} {}] response - {}", request.getMethod(), request.getRequestURI(), result);
        return result;
    }

    @Around("newSpan()")
    public Object newSpanLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String httpMethod = request.getMethod();
        String path = request.getRequestURI();
        String[] packages = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String className = packages[packages.length - 1];
        String methodName = joinPoint.getSignature().getName();

        log.info("[{} {}] {}.{}() call", httpMethod, path, className, methodName);
        return joinPoint.proceed();
    }
}
