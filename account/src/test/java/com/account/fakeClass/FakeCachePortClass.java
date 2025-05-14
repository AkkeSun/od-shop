package com.account.fakeClass;

import com.account.applicaiton.port.out.CachePort;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeCachePortClass implements CachePort {

    @Override
    public void registerToken(Token token) {
        log.info("FakeCachePortClass registerToken");
    }

    @Override
    public void deleteTokenByEmail(String email) {
        log.info("FakeCachePortClass deleteTokenByEmail");
    }

    @Override
    public Token findTokenByEmailAndUserAgent(String email, String userAgent) {
        if (!email.equals("success")) {
            return null;
        }
        return Token.builder()
            .id(1L)
            .accountId(1L)
            .email(email)
            .refreshToken("valid refresh token")
            .userAgent(userAgent)
            .role(Role.ROLE_CUSTOMER.toString())
            .regDateTime(null)
            .build();
    }
}
