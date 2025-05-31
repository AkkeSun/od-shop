package com.accountagent.adapter.out.persistence.mongo;

import static com.accountagent.infrastructure.util.DateUtil.getCurrentDate;

import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;
import com.accountagent.domain.model.LoginLog;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class LogStorageAdapter implements LogStoragePort {

    private final MongoTemplate mongoTemplate;

    @Override
    public void registerHistoryLog(AccountHistory history) {
        mongoTemplate.save(AccountHistoryMapper.toDocument(history), "history_" + getCurrentDate());
    }

    @Override
    public void registerDlqLog(DlqLog dlqLog) {
        mongoTemplate.save(DlqLogMapper.toDocument(dlqLog), "dlq_" + getCurrentDate());
    }

    @Override
    public void registerLoginLog(LoginLog loginLog) {
        mongoTemplate.save(LoginLogMapper.toDocument(loginLog), "login_" + getCurrentDate());
    }

    @Override
    public void deleteLoginLog(String regDate) {
        mongoTemplate.dropCollection("login_" + regDate);
    }

    @Override
    public void deleteHistoryLog(String regDate) {
        mongoTemplate.dropCollection("history_" + regDate);
    }
}
