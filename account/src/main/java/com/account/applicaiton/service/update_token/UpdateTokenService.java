package com.account.applicaiton.service.update_token;

import static com.common.infrastructure.util.JsonUtil.toJsonString;
import static com.common.infrastructure.util.JwtUtil.createAccessToken;
import static com.common.infrastructure.util.JwtUtil.createRefreshToken;
import static com.common.infrastructure.util.JwtUtil.getEmail;
import static com.common.infrastructure.util.JwtUtil.validateTokenExceptExpiration;

import com.account.applicaiton.port.in.UpdateTokenUseCase;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.RefreshTokenInfo;
import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.ErrorCode;
import com.common.infrastructure.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class UpdateTokenService implements UpdateTokenUseCase {

    @Value("${spring.data.redis.key.token}")
    private String tokenRedisKey;
    @Value("${spring.data.redis.ttl.refresh-token}")
    private Long refreshTokenTtl;
    private final UserAgentUtil userAgentUtil;
    private final RedisStoragePort redisStoragePort;

    @Override
    public UpdateTokenServiceResponse update(String refreshToken) {
        if (!validateTokenExceptExpiration(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String email = getEmail(refreshToken);
        String userAgent = userAgentUtil.getUserAgent();
        String redisKey = String.format(tokenRedisKey, email, userAgent);

        RefreshTokenInfo tokenInfo = redisStoragePort.findData(redisKey, RefreshTokenInfo.class);

        if (ObjectUtils.isEmpty(tokenInfo) || tokenInfo.isDifferentRefreshToken(refreshToken)) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        tokenInfo.updateRefreshToken(createRefreshToken(email));
        redisStoragePort.register(redisKey, toJsonString(tokenInfo), refreshTokenTtl);

        return UpdateTokenServiceResponse.builder()
            .accessToken(createAccessToken(
                email, tokenInfo.getAccountId(), tokenInfo.getRoleString()))
            .refreshToken(tokenInfo.getRefreshToken())
            .build();
    }
}
