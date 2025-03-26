package com.accountagent.application.service.register_dql_info;

import static com.accountagent.global.util.JsonUtil.parseJson;

import com.accountagent.application.port.in.RegisterDqlInfoUseCase;
import com.accountagent.application.port.out.RegisterLogPort;
import com.accountagent.domain.model.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterDlqInfoService implements RegisterDqlInfoUseCase {

    private final RegisterLogPort registerLogPort;

    @Override
    public RegisterDqlInfoServiceResponse registerDqlInfo(String payload) {
        DlqLog dlqLog = parseJson(payload, DlqLog.class);
        registerLogPort.registerDlqLog(dlqLog);
        return RegisterDqlInfoServiceResponse.builder().result("Y").build();
    }
}
