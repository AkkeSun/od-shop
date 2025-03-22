package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface UpdateTokenPort {

    void updateToken(Token tokenCache);
}
