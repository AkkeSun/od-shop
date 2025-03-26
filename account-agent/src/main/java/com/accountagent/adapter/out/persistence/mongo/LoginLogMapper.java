package com.accountagent.adapter.out.persistence.mongo;

import com.accountagent.domain.model.LoginLog;

class LoginLogMapper {

    public static LoginLogDocument toDocument(LoginLog domain) {
        return LoginLogDocument.builder()
            .accountId(domain.accountId())
            .email(domain.email())
            .loginDateTime(domain.loginDateTime())
            .build();
    }
}
