package com.account.account.adapter.out.persistence.mongo;

import static com.account.global.util.DateUtil.getCurrentDate;

import com.account.account.applicaiton.port.out.FindAccountHistoryPort;
import com.account.account.applicaiton.port.out.RegisterAccountHistoryPort;
import com.account.account.domain.model.AccountHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountHistoryPersistenceAdapter implements RegisterAccountHistoryPort,
    FindAccountHistoryPort {

    private final AccountHistoryMapper mapper;
    private final MongoTemplate mongoTemplate;
    
    @Override
    public void register(AccountHistory accountHistory) {
        mongoTemplate.save(mapper.toDocument(accountHistory),
            "history_" + getCurrentDate());
    }

    @Override
    public AccountHistory findByEmail(String email) {
        return null;
    }
}
