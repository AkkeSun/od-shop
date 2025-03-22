package com.account.account.applicaiton.service.register_account;

import static com.account.global.exception.ErrorCode.Business_SAVED_ACCOUNT_INFO;
import static com.account.global.util.DateUtil.getCurrentDate;
import static com.account.global.util.DateUtil.getCurrentDateTime;

import com.account.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.account.applicaiton.port.out.FindAccountPort;
import com.account.account.applicaiton.port.out.RegisterAccountPort;
import com.account.account.domain.model.Account;
import com.account.account.domain.model.Role;
import com.account.global.exception.CustomBusinessException;
import com.account.global.util.AesUtil;
import com.account.global.util.JwtUtil;
import com.account.global.util.UserAgentUtil;
import com.account.token.application.port.out.RegisterTokenCachePort;
import com.account.token.application.port.out.RegisterTokenPort;
import com.account.token.domain.model.Token;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class RegisterAccountService implements RegisterAccountUseCase {

    private final AesUtil aesUtil;
    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final FindAccountPort findAccountPort;
    private final RegisterTokenPort registerTokenPort;
    private final RegisterAccountPort registerAccountPort;
    private final RegisterTokenCachePort registerTokenCachePort;

    @Override
    public RegisterAccountServiceResponse registerAccount(RegisterAccountCommand command) {
        if (findAccountPort.existsByEmail(command.email())) {
            throw new CustomBusinessException(Business_SAVED_ACCOUNT_INFO);
        }

        Account account = Account.builder()
            .email(command.email())
            .password(aesUtil.encryptText(command.password()))
            .username(command.username())
            .userTel(command.userTel())
            .address(command.address())
            .role(Role.valueOf(command.role()))
            .regDateTime(LocalDateTime.now())
            .regDate(getCurrentDate())
            .build();
        Account savedAccount = registerAccountPort.register(account);

        String accessToken = jwtUtil.createAccessToken(savedAccount);
        String refreshToken = jwtUtil.createRefreshToken(savedAccount.getEmail());

        Token token = Token.builder()
            .accountId(savedAccount.getId())
            .email(savedAccount.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(getCurrentDateTime())
            .role(command.role())
            .build();

        registerTokenPort.registerToken(token);
        registerTokenCachePort.registerToken(token);

        return RegisterAccountServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }
}
