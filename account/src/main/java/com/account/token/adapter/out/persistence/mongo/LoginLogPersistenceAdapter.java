package com.account.token.adapter.out.persistence.mongo;

import static com.account.global.util.DateUtil.getCurrentDate;

import com.account.token.application.port.out.RegisterLoginLogPort;
import com.account.token.domain.model.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LoginLogPersistenceAdapter implements RegisterLoginLogPort {

    private final LoginLogMapper mapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public void register(Token token) {
        mongoTemplate.save(mapper.toDocument(token), "login_log_"+ getCurrentDate());
    }
}
