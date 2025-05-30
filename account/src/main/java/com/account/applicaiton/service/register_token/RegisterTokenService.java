package com.account.applicaiton.service.register_token;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.LoginLog;
import com.account.domain.model.Token;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = accountStoragePort.findByEmailAndPassword(command.email(),
            command.password());

        String accessToken = jwtUtil.createAccessToken(account);
        String refreshToken = jwtUtil.createRefreshToken(command.email());
        Token token = Token.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(getCurrentDateTime())
            .role(account.getRole().name())
            .build();

        redisStoragePort.registerToken(token);
        tokenStoragePort.registerToken(token);

        LoginLog loginLog = LoginLog.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .loginDateTime(getCurrentDateTime())
            .build();

        messageProducerPort.sendMessage("account-login", toJsonString(loginLog));

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
