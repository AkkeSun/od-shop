package com.account.token.application.port.out;

import com.account.token.domain.model.Token;

public interface FindTokenCachePort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}
