package com.account.applicaiton.service.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    private final JwtUtil jwtUtil;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;

    @Override
    public DeleteTokenServiceResponse deleteToken(Account account) {
        redisStoragePort.deleteTokenByEmail(account.getEmail());
        tokenStoragePort.deleteByEmail(account.getEmail());

        log.info("[Delete token] {}", account.getEmail());
        return DeleteTokenServiceResponse.builder()
            .result("Y")
            .build();
    }
}
