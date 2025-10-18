package com.accountagent.application.service.register_dql_info;

import static com.common.infrastructure.util.DateUtil.getCurrentDateTime;

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
    public void registerDqlInfo(String topic, String payload) {
        logStoragePort.registerDlqLog(DlqLog.builder()
            .topic(topic)
            .payload(payload)
            .regDateTime(getCurrentDateTime())
            .build());
    }
}
