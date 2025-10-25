package com.accountagent.fakeClass;

import com.accountagent.application.port.in.RegisterLoginLogUseCase;
import com.accountagent.application.service.register_login_log.RegisterLoginLogServiceResponse;
import java.util.ArrayList;
import java.util.List;

public class FakeRegisterLoginLogUseCase implements RegisterLoginLogUseCase {

    public List<String> receivedPayloads = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public RegisterLoginLogServiceResponse registerLoginLog(String payload) {
        if (shouldThrowException) {
            throw new RuntimeException("Processing failed");
        }
        receivedPayloads.add(payload);
        return RegisterLoginLogServiceResponse.builder()
            .result("Y")
            .build();
    }

    public void clear() {
        receivedPayloads.clear();
        shouldThrowException = false;
    }
}
