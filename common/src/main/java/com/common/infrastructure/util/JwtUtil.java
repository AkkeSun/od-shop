package com.common.infrastructure.util;

import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

public class JwtUtil {

    private final static String jwtKey = "od-shop-jwt";

    private final static long tokenValidTime = 600000; // 10 min

    private final static long refreshTokenValidTime = 259200000; // 3 days

    public static String createAccessToken(String email, Long accountId, String roles) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("accountId", accountId);
        claims.put("roles", roles);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + tokenValidTime))
            .signWith(SignatureAlgorithm.HS256, jwtKey)
            .compact();
    }

    public static String createRefreshToken(String email) {
        Date now = new Date();
        Claims claims = Jwts.claims().setSubject(email);
        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenValidTime))
            .signWith(SignatureAlgorithm.HS256, jwtKey)
            .compact();
    }

    public static boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public static String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    public static Long getAccountId(String token) {
        return Long.valueOf(getClaims(token).get("accountId").toString());
    }

    public static Claims getClaims(String token) {
        try {
            token = token.replace("Bearer ", "");
            return Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }
}
