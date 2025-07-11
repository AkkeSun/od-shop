package com.product.infrastructure.aop;

import static com.product.infrastructure.util.JsonUtil.toObjectNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.product.domain.model.Account;
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

    @Pointcut("@annotation(io.micrometer.tracing.annotation.NewSpan))")
    private void newSpan() {
    }

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void controllerMethods() {
    }

    @Pointcut("@annotation(com.product.infrastructure.aop.ExceptionHandlerLog))")
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
            if (arg instanceof Account) {
                userInfo = toObjectNode(arg);
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

    @Around("newSpan()")
    public Object newSpanLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String[] packages = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String className = packages[packages.length - 1];
        String methodName = joinPoint.getSignature().getName();

        HttpServletRequest request = getRequest();
        if (request == null) {
            log.info("[gRPC] {}.{}() call", className, methodName);
        } else {
            String httpMethod = request.getMethod();
            String path = request.getRequestURI();

            log.info("[{} {}] {}.{}() call", httpMethod, path, className, methodName);
        }
        return joinPoint.proceed();
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
