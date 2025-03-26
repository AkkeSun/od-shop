package com.accountagent.adapter.out.persistence.mongo;

import static com.accountagent.global.util.DateUtil.getCurrentDate;

import com.accountagent.application.port.out.RegisterLogPort;
import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogPersistenceAdapter implements RegisterLogPort {

    private final MongoTemplate mongoTemplate;
    private final DlqLogMapper dlqLogMapper;
    private final AccountHistoryMapper accountHistoryMapper;

    @Override
    public void registerHistoryLog(AccountHistory history) {
        mongoTemplate.save(accountHistoryMapper.toDocument(history), "history_" + getCurrentDate());
    }

    @Override
    public void registerDlqLog(DlqLog dlqLog) {
        mongoTemplate.save(dlqLogMapper.toDocument(dlqLog), "dlq_" + getCurrentDate());
    }
}
