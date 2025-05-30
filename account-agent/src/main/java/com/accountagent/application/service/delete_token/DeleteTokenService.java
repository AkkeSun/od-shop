package com.accountagent.application.service.delete_token;

import com.accountagent.application.port.in.DeleteTokenUseCase;
import com.accountagent.application.port.out.TokenStoragePort;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    private final TokenStoragePort tokenStoragePort;

    @Override
    public void deleteToken(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        LocalDateTime start = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = date.withHour(23).withMinute(59).withSecond(59);
        tokenStoragePort.deleteByRegDateTimeBetween(start.format(formatter), end.format(formatter));
    }
}
