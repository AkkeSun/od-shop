package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface UpdateTokenPort {

    void updateToken(Token tokenCache);
}
