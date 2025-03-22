package com.account.global.filter;

import com.account.global.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String token = request.getHeader("Authorization");
            if (jwtUtil.validateTokenExceptExpiration(token)) {
                SecurityContextHolder.getContext().setAuthentication(makeAuthToken(token));
            }
        } catch (Exception ignored) {
            // 예외는 authenticationEntryPoint 에서 처리합니다.
        }

        filterChain.doFilter(request, response);
    }

    private Authentication makeAuthToken(String token) {
        Claims claims = jwtUtil.getClaims(token);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "",
            List.of(new SimpleGrantedAuthority((String) claims.get("role"))));
    }
}
