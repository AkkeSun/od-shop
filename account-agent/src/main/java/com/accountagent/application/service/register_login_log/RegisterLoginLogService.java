package com.accountagent.application.service.register_login_log;

import com.accountagent.application.port.in.RegisterLoginLogUseCase;
import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.LoginLog;
import com.accountagent.infrastructure.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterLoginLogService implements RegisterLoginLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public RegisterLoginLogServiceResponse registerLoginLog(String payload) {
        LoginLog loginLog = JsonUtil.parseJson(payload, LoginLog.class);
        logStoragePort.registerLoginLog(loginLog);
        return RegisterLoginLogServiceResponse.builder().result("Y").build();
    }
}