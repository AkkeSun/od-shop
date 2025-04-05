package com.accountagent.application.service.delete_log;

import com.accountagent.application.port.in.DeleteLogUseCase;
import com.accountagent.application.port.out.LogStoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteLogService implements DeleteLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void deleteLog(String regDate) {
        logStoragePort.deleteHistoryLog(regDate);
        logStoragePort.deleteLoginLog(regDate);
    }
}
