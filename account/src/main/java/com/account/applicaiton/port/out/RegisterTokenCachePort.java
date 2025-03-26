package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface RegisterTokenCachePort {

    void registerToken(Token token);
}
