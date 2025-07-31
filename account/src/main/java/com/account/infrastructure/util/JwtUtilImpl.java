package com.account.infrastructure.util;

import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilImpl implements JwtUtil {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Value("${spring.data.redis.ttl.access-token}")
    private long tokenValidTime;

    @Value("${spring.data.redis.ttl.refresh-token}")
    private long refreshTokenValidTime;

    @Override
    public String createAccessToken(Account account) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(account.getEmail());
        claims.put("accountId", account.getId());
        claims.put("roles", account.getRoles().stream()
            .map(Role::name)
            .collect(Collectors.joining(",")));
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    @Override
    public String createRefreshToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    @Override
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    @Override
    public Long getAccountId(String token) {
        return Long.valueOf(getClaims(token).get("accountId").toString());
    }

    @Override
    public Claims getClaims(String token) {
        try {
            token = token.replace("Bearer ", "");
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
