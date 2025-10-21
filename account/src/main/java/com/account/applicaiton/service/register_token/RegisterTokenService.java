package com.account.applicaiton.service.register_token;

import static com.common.infrastructure.util.JsonUtil.toJsonString;
import static com.common.infrastructure.util.JwtUtil.createAccessToken;
import static com.common.infrastructure.util.JwtUtil.createRefreshToken;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.LoginLog;
import com.account.domain.model.RefreshTokenInfo;
import com.account.domain.model.Role;
import com.account.infrastructure.properties.KafkaTopicProperties;
import com.account.infrastructure.properties.RedisProperties;
import com.common.infrastructure.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    private final RedisProperties redisProperties;
    private final KafkaTopicProperties topicProperties;
    private final UserAgentUtil userAgentUtil;
    private final RedisStoragePort redisStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = accountStoragePort.findByEmailAndPassword(command.email(),
            command.password());

        RefreshTokenInfo refreshTokenInfo = RefreshTokenInfo.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(createRefreshToken(command.email()))
            .roles(account.getRoles().stream()
                .map(Role::name)
                .toList())
            .build();

        redisStoragePort.register(
            String.format(redisProperties.key().token(), account.getEmail(),
                refreshTokenInfo.getUserAgent()),
            toJsonString(refreshTokenInfo),
            redisProperties.ttl().refreshToken()
        );
        messageProducerPort.sendMessage(topicProperties.login(),
            toJsonString(LoginLog.of(account)));

        return RegisterTokenServiceResponse.builder()
            .accessToken(createAccessToken(
                account.getEmail(), account.getId(), account.getRoleString()))
            .refreshToken(refreshTokenInfo.getRefreshToken())
            .build();
    }
}
