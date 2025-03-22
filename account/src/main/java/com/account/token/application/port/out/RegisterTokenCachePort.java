package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface RegisterTokenCachePort {

    void registerToken(Token token);
}
