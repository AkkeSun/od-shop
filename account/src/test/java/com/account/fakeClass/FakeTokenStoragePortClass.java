package com.account.fakeClass;

import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeTokenStoragePortClass implements TokenStoragePort {

    @Override
    public void registerToken(Token token) {
        log.info("FakeTokenStoragePortClass registerToken");
    }

    @Override
    public void deleteByEmail(String email) {
        log.info("FakeTokenStoragePortClass deleteByEmail");
    }

    @Override
    public Token findByEmailAndUserAgent(String email, String userAgent) {
        if (email.equals("success2") || email.equals("success3")) {
            return Token.builder()
                .id(1L)
                .accountId(1L)
                .email(email)
                .refreshToken("valid refresh token2")
                .userAgent(userAgent)
                .role(Role.ROLE_CUSTOMER.toString())
                .regDateTime(null)
                .build();
        }
        return null;
    }

    @Override
    public void updateToken(Token tokenCache) {
        log.info("FakeTokenStoragePortClass updateToken");
    }
}
