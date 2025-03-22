package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface RegisterTokenPort {

    void registerToken(Token token);
}
