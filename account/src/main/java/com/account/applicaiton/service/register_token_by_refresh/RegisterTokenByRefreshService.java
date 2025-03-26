package com.account.applicaiton.service.register_token_by_refresh;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;

import com.account.applicaiton.port.in.RegisterTokenByRefreshUseCase;
import com.account.applicaiton.port.out.FindTokenCachePort;
import com.account.applicaiton.port.out.FindTokenPort;
import com.account.applicaiton.port.out.RegisterTokenCachePort;
import com.account.applicaiton.port.out.UpdateTokenPort;
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
    private final FindTokenPort findTokenPort;
    private final UserAgentUtil userAgentUtil;
    private final UpdateTokenPort updateTokenPort;
    private final FindTokenCachePort findTokenCachePort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterTokenByRefreshServiceResponse registerTokenByRefresh(String refreshToken) {
        if (!jwtUtil.validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = jwtUtil.getEmail(refreshToken);
        String userAgent = userAgentUtil.getUserAgent();
        Token savedToken = findTokenCachePort.findByEmailAndUserAgent(email, userAgent);

        if (ObjectUtils.isEmpty(savedToken)) {
            log.info("[Token cache notfound] {} - {}", email, userAgent);
            savedToken = findTokenPort.findByEmailAndUserAgent(email, userAgent);
        }
        if (ObjectUtils.isEmpty(savedToken) || savedToken.isDifferentRefreshToken(refreshToken)) {
            log.info("[Invalid token] {} - {}", email, userAgent);
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Account account = savedToken.toAccount();
        String newAccessToken = jwtUtil.createAccessToken(account);
        String newRefreshToken = jwtUtil.createRefreshToken(email);

        savedToken.updateRefreshToken(newRefreshToken);
        savedToken.updateRegTime(getCurrentDateTime());

        updateTokenPort.updateToken(savedToken);
        registerTokenCachePort.registerToken(savedToken);

        return RegisterTokenByRefreshServiceResponse.builder()
            .accessToken(newAccessToken)
            .refreshToken(newRefreshToken)
            .build();
    }
}
