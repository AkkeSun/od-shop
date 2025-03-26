package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface FindTokenCachePort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}
