package com.account.applicaiton.service.find_account;

import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.port.out.FindAccountPort;
import com.account.domain.model.Account;
import com.account.infrastructure.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindAccountService implements FindAccountInfoUseCase {

    private final JwtUtil jwtUtil;
    private final FindAccountPort findAccountPort;

    @Override
    public FindAccountServiceResponse findAccountInfo(String authorization) {
        Long accountId = jwtUtil.getAccountId(authorization);
        Account account = findAccountPort.findById(accountId);

        return FindAccountServiceResponse.builder()
            .id(account.getId())
            .email(account.getEmail())
            .username(account.getUsername())
            .userTel(account.getUserTel())
            .address(account.getAddress())
            .role(account.getRole().toString())
            .regDate(account.getRegDate())
            .build();
    }
}
