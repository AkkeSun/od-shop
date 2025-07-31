package com.account.applicaiton.service.register_account;

import static com.account.infrastructure.exception.ErrorCode.Business_SAVED_ACCOUNT_INFO;
import static com.account.infrastructure.util.DateUtil.getCurrentDate;
import static com.account.infrastructure.util.DateUtil.getCurrentDateTime;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.RoleStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import com.account.domain.model.Role;
import com.account.domain.model.Token;
import com.account.infrastructure.exception.CustomBusinessException;
import com.account.infrastructure.exception.CustomValidationException;
import com.account.infrastructure.util.JwtUtil;
import com.account.infrastructure.util.UserAgentUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class RegisterAccountService implements RegisterAccountUseCase {

    @Value("${spring.data.redis.key.token}")
    private String tokenRedisKey;
    @Value("${spring.data.redis.ttl.refresh-token}")
    private Long refreshTokenTtl;
    private final JwtUtil jwtUtil;
    private final UserAgentUtil userAgentUtil;
    private final RoleStoragePort roleStoragePort;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;
    private final AccountStoragePort accountStoragePort;

    @Override
    public RegisterAccountServiceResponse registerAccount(RegisterAccountCommand command) {
        if (accountStoragePort.existsByEmail(command.email())) {
            throw new CustomBusinessException(Business_SAVED_ACCOUNT_INFO);
        }

        Map<String, Role> roleMap = getRoleMap();
        List<Role> roles = command.roles().stream()
            .map(r -> {
                if (!roleMap.containsKey(r)) {
                    throw new CustomValidationException("존재하지 않는 권한 입니다");
                }
                return roleMap.get(r);
            })
            .toList();

        Account account = Account.builder()
            .email(command.email())
            .password(command.password())
            .username(command.username())
            .userTel(command.userTel())
            .address(command.address())
            .roles(roles)
            .regDateTime(LocalDateTime.now())
            .regDate(getCurrentDate())
            .build();
        Account savedAccount = accountStoragePort.register(account);

        String accessToken = jwtUtil.createAccessToken(savedAccount);
        String refreshToken = jwtUtil.createRefreshToken(savedAccount.getEmail());

        Token token = Token.builder()
            .accountId(savedAccount.getId())
            .email(savedAccount.getEmail())
            .userAgent(userAgentUtil.getUserAgent())
            .refreshToken(refreshToken)
            .regDateTime(getCurrentDateTime())
            .roles(String.join(",", command.roles()))
            .build();

        redisStoragePort.register(
            String.format(tokenRedisKey, token.getEmail(), token.getUserAgent()),
            toJsonString(token),
            refreshTokenTtl
        );
        tokenStoragePort.registerToken(token);

        return RegisterAccountServiceResponse.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    private Map<String, Role> getRoleMap() {
        List<Role> roles = redisStoragePort.findDataList("roles", Role.class);
        if (roles.isEmpty()) {
            roles = roleStoragePort.findAll();
            redisStoragePort.register("roles", toJsonString(roles), 86400);
        }

        return roles.stream().collect(Collectors.toMap(Role::name, Function.identity()));
    }
}
