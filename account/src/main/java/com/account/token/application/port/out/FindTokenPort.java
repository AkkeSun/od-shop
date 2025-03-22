package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface FindTokenPort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}
