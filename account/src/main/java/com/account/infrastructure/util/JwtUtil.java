package com.account.infrastructure.util;

import com.account.domain.model.Account;
import io.jsonwebtoken.Claims;

public interface JwtUtil {

    String createAccessToken(Account account);

    String createRefreshToken(String email);

    boolean validateTokenExceptExpiration(String token);

    String getEmail(String token);

    Long getAccountId(String token);

    Claims getClaims(String token);
}
