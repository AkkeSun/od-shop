package com.accountagent.application.port.out;

import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;

public interface RegisterLogPort {

    void registerHistoryLog(AccountHistory history);

    void registerDlqLog(DlqLog dlqLog);
}
