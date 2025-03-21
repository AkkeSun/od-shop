package com.account.global.util;

import com.account.account.domain.model.Account;
import io.jsonwebtoken.Claims;

public interface JwtUtil  {

     String createAccessToken(Account account);

     String createRefreshToken(String email) ;

     boolean validateTokenExceptExpiration(String token);

    String getEmail(String token);

     Long getAccountId(String token);

    Claims getClaims(String token);
}
