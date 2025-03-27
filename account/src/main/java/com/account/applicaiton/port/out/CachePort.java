package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface CachePort {

    void registerToken(Token token);

    void deleteTokenByEmail(String email);

    Token findTokenByEmailAndUserAgent(String email, String userAgent);

}
