package com.accountagent.fakeClass;

import com.accountagent.application.port.in.RegisterHistoryUseCase;
import com.accountagent.application.service.register_history.RegisterHistoryServiceResponse;
import java.util.ArrayList;
import java.util.List;

public class FakeRegisterHistoryUseCase implements RegisterHistoryUseCase {

    public List<String> receivedPayloads = new ArrayList<>();
    public boolean shouldThrowException = false;

    @Override
    public RegisterHistoryServiceResponse registerHistory(String payload) {
        if (shouldThrowException) {
            throw new RuntimeException("Processing failed");
        }
        receivedPayloads.add(payload);
        return RegisterHistoryServiceResponse.builder()
            .result("Y")
            .build();
    }

    public void clear() {
        receivedPayloads.clear();
        shouldThrowException = false;
    }
}
