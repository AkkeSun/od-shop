package com.accountagent.application.service.register_dql_info;

import static com.accountagent.infrastructure.util.JsonUtil.parseJson;

import com.accountagent.application.port.in.RegisterDqlInfoUseCase;
import com.accountagent.application.port.out.LogStoragePort;
import com.accountagent.domain.model.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterDlqInfoService implements RegisterDqlInfoUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public RegisterDqlInfoServiceResponse registerDqlInfo(String payload) {
        DlqLog dlqLog = parseJson(payload, DlqLog.class);
        logStoragePort.registerDlqLog(dlqLog);
        return RegisterDqlInfoServiceResponse.builder().result("Y").build();
    }
}
