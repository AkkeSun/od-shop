package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface TokenStoragePort {

    void registerToken(Token token);

    void deleteByEmail(String email);

    Token findByEmailAndUserAgent(String email, String userAgent);

    void updateToken(Token tokenCache);
}
