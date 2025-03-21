package com.account.global.util;

import com.account.account.domain.model.Account;
import com.account.global.exception.CustomAuthenticationException;
import com.account.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtilImpl implements JwtUtil{

    @Value("${jwt.token.secret-key}")
    private static String secretKey;

    @Value("${jwt.token.valid-time}")
    private static long tokenValidTime;

    @Value("${jwt.token.refresh-valid-time}")
    private static long refreshTokenValidTime;

    @Override
    public String createAccessToken(Account account) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(account.getEmail());
        claims.put("accountId", account.getId());
        claims.put("role", account.getRole());
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
        return (Long) getClaims(token).get("accountId");
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
