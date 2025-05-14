package com.account.fakeClass;

import com.account.domain.model.Account;
import com.account.infrastructure.util.JwtUtil;
import io.jsonwebtoken.Claims;

public class FakeJwtUtilClass implements JwtUtil {

    @Override
    public String createAccessToken(Account account) {
        return "valid token";
    }

    @Override
    public String createRefreshToken(String email) {
        return "valid refresh token";
    }

    @Override
    public boolean validateTokenExceptExpiration(String token) {
        return token.contains("valid token") || token.contains("valid refresh token");
    }

    @Override
    public String getEmail(String token) {
        if (token.equals("valid token") || token.equals("valid refresh token")) {
            return "success";
        }
        if (token.equals("valid refresh token2")) {
            return "success2";
        }
        return "error";
    }

    @Override
    public Long getAccountId(String token) {
        return 1L;
    }

    @Override
    public Claims getClaims(String token) {
        return null;
    }
}
