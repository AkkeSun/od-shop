package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface RegisterLoginLogPort {
    void register(Token token);
}
