package com.accountagent.application.port.in;

import java.time.LocalDateTime;

public interface DeleteTokenUseCase {

    void deleteToken(LocalDateTime date);
}
