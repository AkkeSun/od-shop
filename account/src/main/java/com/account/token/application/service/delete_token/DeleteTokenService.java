package com.account.token.application.service.delete_token;

import com.account.global.util.JwtUtil;
import com.account.token.application.port.in.DeleteTokenUseCase;
import com.account.token.application.port.out.DeleteTokenCachePort;
import com.account.token.application.port.out.DeleteTokenPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    private final JwtUtil jwtUtil;
    private final DeleteTokenPort deleteTokenPort;
    private final DeleteTokenCachePort deleteTokenCachePort;

    @Override
    public DeleteTokenServiceResponse deleteToken(String accessToken) {
        String email = jwtUtil.getEmail(accessToken);
        deleteTokenCachePort.deleteByEmail(email);
        deleteTokenPort.deleteByEmail(email);

        log.info("[Delete token] {}", email);
        return DeleteTokenServiceResponse.builder()
            .result("Y")
            .build();
    }
}
