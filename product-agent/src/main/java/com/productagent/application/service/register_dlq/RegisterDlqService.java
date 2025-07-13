package com.productagent.application.service.register_dlq;

import static com.productagent.infrastructure.util.JsonUtil.getJsonNode;

import com.fasterxml.jackson.databind.JsonNode;
import com.productagent.application.port.in.RegisterDlqUseCase;
import com.productagent.application.port.out.LogStoragePort;
import com.productagent.domain.model.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterDlqService implements RegisterDlqUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void register(String payload) {
        JsonNode jsonNode = getJsonNode(payload);
        if (jsonNode == null) {
            return;
        }
        logStoragePort.registerDlqLog(DlqLog.of(jsonNode));
    }
}
