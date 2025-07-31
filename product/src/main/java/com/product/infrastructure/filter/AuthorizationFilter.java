package com.product.infrastructure.filter;

import com.product.application.port.out.AuthorizationStoragePort;
import com.product.domain.model.AuthorizationRule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.server.PathContainer;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

    private final AuthorizationStoragePort authorizationStoragePort;
    private final PathPatternParser parser;

    AuthorizationFilter(AuthorizationStoragePort authorizationStoragePort) {
        this.authorizationStoragePort = authorizationStoragePort;
        this.parser = new PathPatternParser();
    }

    /*
        [ Rule ]
        1. 등록된 인가 정책의 권한이 ANONYMOUS 인 경우 누구나 접근 허용
        2. 등록된 인가 정책에 따라 접근 허용
        3. 미등록된 URI 의 경우 인증된 사용자라면 누구나 접근 허용
     */
    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
        FilterChain filterChain) throws ServletException, IOException {

        String requestUri = req.getRequestURI();
        String requestMethod = req.getMethod();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        for (AuthorizationRule rule : authorizationStoragePort.findAll()) {
            if (!rule.httpMethod().equalsIgnoreCase(requestMethod)) {
                continue;
            }

            PathPattern pattern = parser.parse(rule.uriPattern());
            if (!pattern.matches(PathContainer.parsePath(requestUri))) {
                continue;
            }

            if(rule.isAnonymous()){
                filterChain.doFilter(req, res);
                return;
            }

            List<String> userRoles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

            if (userRoles.contains(rule.role())) {
                filterChain.doFilter(req, res);
                return;
            } else {
                throw new AccessDeniedException("e");
            }
        }

        if (auth.getPrincipal().toString().contains("anonymous")) {
            throw new AccessDeniedException("e");
        }

        filterChain.doFilter(req, res);
    }
}
