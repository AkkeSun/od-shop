package com.account.applicaiton.service.register_token;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.RefreshTokenInfoStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.LoginLog;
import com.account.domain.model.RefreshTokenInfo;
import com.account.domain.model.Role;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    @Value("${kafka.topic.login}")
    private String loginTopic;
    @Value("${spring.data.redis.key.token}")
    private String tokenRedisKey;
    @Value("${spring.data.redis.ttl.refresh-token}")
    private Long refreshTokenTtl;
    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final RedisStoragePort redisStoragePort;
    private final RefreshTokenInfoStoragePort refreshTokenInfoStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = accountStoragePort.findByEmailAndPassword(command.email(),
            command.password());

        String accessToken = jwtUtil.createAccessToken(account);
        String refreshToken = jwtUtil.createRefreshToken(command.email());
        RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(getCurrentDateTime())
            .roles(account.getRoles().stream()
                .map(Role::name)
                .collect(Collectors.joining(",")))
            .build();

        redisStoragePort.register(
            String.format(tokenRedisKey, account.getEmail(), refreshTokenInfo.getUserAgent()),
            toJsonString(refreshTokenInfo),
            refreshTokenTtl
        );
        refreshTokenInfoStoragePort.registerToken(refreshTokenInfo);
        messageProducerPort.sendMessage(loginTopic, toJsonString(LoginLog.of(account)));

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
