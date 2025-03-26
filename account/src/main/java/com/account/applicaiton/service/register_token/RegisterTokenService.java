package com.account.applicaiton.service.register_token;

import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;

import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.port.out.FindAccountPort;
import com.account.applicaiton.port.out.RegisterLoginLogPort;
import com.account.applicaiton.port.out.RegisterTokenCachePort;
import com.account.applicaiton.port.out.RegisterTokenPort;
import com.account.domain.model.Account;
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
    private final RegisterLoginLogPort registerLoginLogPort;
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

        registerLoginLogPort.register(token);
        registerTokenPort.registerToken(token);
        registerTokenCachePort.registerToken(token);

        return RegisterTokenServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
