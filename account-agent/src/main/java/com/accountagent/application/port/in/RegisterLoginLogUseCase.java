package com.accountagent.application.port.in;

import com.accountagent.application.service.register_login_log.RegisterLoginLogServiceResponse;

public interface RegisterLoginLogUseCase {

    RegisterLoginLogServiceResponse registerLoginLog(String payload);
}
