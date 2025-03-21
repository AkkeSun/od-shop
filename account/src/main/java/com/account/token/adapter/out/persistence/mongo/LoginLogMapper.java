package com.account.token.adapter.out.persistence.mongo;

import static com.account.global.util.DateUtil.getCurrentDateTime;

import com.account.token.domain.model.Token;
import org.springframework.stereotype.Component;

@Component
class LoginLogMapper {

    public LoginLogDocument toDocument(Token token) {
        return LoginLogDocument.builder()
            .accountId(token.getAccountId())
            .email(token.getEmail())
            .loginDateTime(getCurrentDateTime())
            .build();
    }
}
