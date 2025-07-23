package com.productagent.application.service;

import com.productagent.application.port.in.DeleteLogUseCase;
import com.productagent.application.port.out.LogStoragePort;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteLogService implements DeleteLogUseCase {

    private final LogStoragePort logStoragePort;

    @Override
    public void delete(LocalDateTime targetDate) {
        logStoragePort.dropCollection(targetDate);
    }
}
