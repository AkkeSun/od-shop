package com.productagent.application.port.in;

import java.time.LocalDateTime;

public interface DeleteLogUseCase {

    void delete(LocalDateTime targetDate);
}
