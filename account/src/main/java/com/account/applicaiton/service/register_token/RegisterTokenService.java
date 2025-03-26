package com.account.applicaiton.service.register_token;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.FindAccountPort;
import com.account.applicaiton.port.out.ProduceAccountPort;
import com.account.applicaiton.port.out.RegisterTokenCachePort;
import com.account.applicaiton.port.out.RegisterTokenPort;
import com.account.domain.model.Account;
import com.account.domain.model.LoginLog;
import com.account.domain.model.Token;
import com.account.infrastructure.util.AesUtil;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class RegisterTokenService implements RegisterTokenUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final FindAccountPort findAccountPort;
    private final RegisterTokenPort registerTokenPort;
    private final ProduceAccountPort produceAccountPort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterTokenServiceResponse registerToken(RegisterTokenCommand command) {
        Account account = findAccountPort.findByEmailAndPassword(
            command.email(), aesUtil.encryptText(command.password()));

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

        registerTokenPort.registerToken(token);
        registerTokenCachePort.registerToken(token);

        LoginLog loginLog = LoginLog.builder()
            .accountId(account.getId())
            .email(account.getEmail())
            .loginDateTime(getCurrentDateTime())
            .build();

        produceAccountPort.sendMessage("login-log", toJsonString(loginLog));

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
