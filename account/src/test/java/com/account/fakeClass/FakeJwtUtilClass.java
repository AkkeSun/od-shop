package com.account.fakeClass;

import com.account.domain.model.Account;
import io.jsonwebtoken.Claims;

public class FakeJwtUtilClass implements JwtUtil {

    @Override
    public String createAccessToken(Account account) {
        return "valid token - " + account.getEmail();
    }

    @Override
    public String createRefreshToken(String email) {
        return "valid refresh token - " + email;
    }

    @Override
    public boolean validateTokenExceptExpiration(String token) {
        return token.contains("valid token") || token.contains("valid refresh token");
    }

    @Override
    public String getEmail(String token) {
        if (validateTokenExceptExpiration(token)) {
            return token.replace("valid token - ", "")
                .replace("valid refresh token - ", "");
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
