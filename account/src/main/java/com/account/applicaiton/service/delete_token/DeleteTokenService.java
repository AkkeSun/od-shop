package com.account.applicaiton.service.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.port.out.CachePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    private final JwtUtil jwtUtil;
    private final CachePort cachePort;
    private final TokenStoragePort tokenStoragePort;

    @Override
    public DeleteTokenServiceResponse deleteToken(String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        cachePort.deleteByEmail(email);
        tokenStoragePort.deleteByEmail(email);

        log.info("[Delete token] {}", email);
        return DeleteTokenServiceResponse.builder()
            .result("Y")
            .build();
    }
}
