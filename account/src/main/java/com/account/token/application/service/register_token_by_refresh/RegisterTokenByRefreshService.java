package com.account.token.application.service.register_token_by_refresh;

import static com.account.global.util.DateUtil.getCurrentDateTime;

import com.account.account.domain.model.Account;
import com.account.global.exception.CustomAuthenticationException;
import com.account.global.exception.ErrorCode;
import com.account.global.util.JwtUtil;
import com.account.global.util.UserAgentUtil;
import com.account.token.application.port.in.RegisterTokenByRefreshUseCase;
import com.account.token.application.port.out.FindTokenCachePort;
import com.account.token.application.port.out.FindTokenPort;
import com.account.token.application.port.out.RegisterTokenCachePort;
import com.account.token.application.port.out.UpdateTokenPort;
import com.account.token.domain.model.Token;
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
