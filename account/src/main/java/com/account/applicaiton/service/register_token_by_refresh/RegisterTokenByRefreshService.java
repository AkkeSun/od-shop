package com.account.applicaiton.service.register_token_by_refresh;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;

import com.account.applicaiton.port.in.RegisterTokenByRefreshUseCase;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenByRefreshService implements RegisterTokenByRefreshUseCase {

    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;

    @Override
    public RegisterTokenByRefreshServiceResponse registerTokenByRefresh(String refreshToken) {
        if (!jwtUtil.validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmail(refreshToken);
        String userAgent = userAgentUtil.getUserAgent();
        Token savedToken = redisStoragePort.findTokenByEmailAndUserAgent(email, userAgent);

        if (ObjectUtils.isEmpty(savedToken)) {
            log.info("[token cache notfound] {} - {}", email, userAgent);
            savedToken = tokenStoragePort.findByEmailAndUserAgent(email, userAgent);
        }
        if (ObjectUtils.isEmpty(savedToken) || savedToken.isDifferentRefreshToken(refreshToken)) {
            log.info("[invalid refresh token] {} - {}", email, userAgent);
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Account account = savedToken.toAccount();
        String newAccessToken = jwtUtil.createAccessToken(account);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        savedToken.updateRefreshToken(newRefreshToken);
        savedToken.updateRegTime(getCurrentDateTime());

        redisStoragePort.registerToken(savedToken);
        tokenStoragePort.updateToken(savedToken);

        return RegisterTokenByRefreshServiceResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
    }
}
