package com.account.applicaiton.service.register_account;

import static com.account.infrastructure.exception.ErrorCode.Business_SAVED_ACCOUNT_INFO;
import static com.account.infrastructure.util.DateUtil.getCurrentDate;
import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;

import com.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.applicaiton.port.out.FindAccountPort;
import com.account.applicaiton.port.out.RegisterAccountPort;
import com.account.applicaiton.port.out.RegisterTokenCachePort;
import com.account.applicaiton.port.out.RegisterTokenPort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomBusinessException;
import com.account.infrastructure.util.AesUtil;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
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
