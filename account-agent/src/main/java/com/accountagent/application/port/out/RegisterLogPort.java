package com.accountagent.application.port.out;

import com.accountagent.domain.model.AccountHistory;
import com.accountagent.domain.model.DlqLog;
import com.accountagent.domain.model.LoginLog;

public interface RegisterLogPort {

    void registerHistoryLog(AccountHistory history);

    void registerDlqLog(DlqLog dlqLog);

    void registerLoginLog(LoginLog loginLog);
}
