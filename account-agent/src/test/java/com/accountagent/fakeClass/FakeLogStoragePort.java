package com.accountagent.fakeClass;

import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;
import com.accountagent.domain.model.LoginLog;
import java.util.ArrayList;
import java.util.List;

public class FakeLogStoragePort implements LogStoragePort {

    public List<AccountHistory> accountHistoryDatabase = new ArrayList<>();
    public List<LoginLog> loginLogDatabase = new ArrayList<>();
    public List<DlqLog> dlqLogDatabase = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public void registerHistoryLog(AccountHistory history) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }
        accountHistoryDatabase.add(history);
        System.out.println("FakeLogStoragePort saved AccountHistory: accountId=" + history.accountId());
    }

    @Override
    public void registerDlqLog(DlqLog dlqLog) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }
        dlqLogDatabase.add(dlqLog);
        System.out.println("FakeLogStoragePort saved DlqLog: topic=" + dlqLog.topic());
    }

    @Override
    public void registerLoginLog(LoginLog loginLog) {
        if (shouldThrowException) {
            throw new RuntimeException("Simulated exception");
        }
        loginLogDatabase.add(loginLog);
        System.out.println("FakeLogStoragePort saved LoginLog: accountId=" + loginLog.accountId());
    }

    @Override
    public void deleteLoginLog(String regDate) {
        loginLogDatabase.removeIf(log -> log.loginDateTime().startsWith(regDate));
        System.out.println("FakeLogStoragePort deleted LoginLog: regDate=" + regDate);
    }

    @Override
    public void deleteHistoryLog(String regDate) {
        accountHistoryDatabase.removeIf(history -> history.regDateTime().startsWith(regDate));
        System.out.println("FakeLogStoragePort deleted AccountHistory: regDate=" + regDate);
    }

    public void clear() {
        accountHistoryDatabase.clear();
        loginLogDatabase.clear();
        dlqLogDatabase.clear();
        shouldThrowException = false;
    }
}
