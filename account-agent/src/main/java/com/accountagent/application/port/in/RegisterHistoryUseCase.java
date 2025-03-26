package com.accountagent.application.port.in;

import com.accountagent.application.service.register_history.RegisterHistoryServiceResponse;

public interface RegisterHistoryUseCase {

    RegisterHistoryServiceResponse registerHistory(String payload);
}
