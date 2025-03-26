package com.account.applicaiton.port.out;

import com.account.domain.model.Token;

public interface FindTokenPort {

    Token findByEmailAndUserAgent(String email, String userAgent);
}
