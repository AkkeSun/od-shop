package com.account.account.applicaiton.service.update_account;

import com.account.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.account.applicaiton.port.out.FindAccountPort;
import com.account.account.applicaiton.port.out.RegisterAccountHistoryPort;
import com.account.account.applicaiton.port.out.UpdateAccountPort;
import com.account.account.domain.model.Account;
import com.account.account.domain.model.AccountHistory;
import com.account.global.util.AesUtil;
import com.account.global.util.JwtUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateAccountService implements UpdateAccountUseCase {

    private final JwtUtil jwtUtil;
    private final AesUtil aesUtil;
    private final FindAccountPort findAccountPort;
    private final UpdateAccountPort updateAccountPort;
    private final RegisterAccountHistoryPort registerAccountHistoryPort;

    @Override
    public UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command) {
        Long accountId = jwtUtil.getAccountId(command.accessToken());
        Account account = findAccountPort.findById(accountId);

        List<String> updateList = new ArrayList<>();
        if (command.isUsernameUpdateRequired(account.getUsername())) {
            updateList.add("username");
            account.updateUsername(command.username());
        }
        if (command.isPasswordUpdateRequired(account.getPassword())) {
            updateList.add("password");
            account.updatePassword(aesUtil.encryptText(command.password()));
        }
        if (command.isUserTelUpdateRequired(account.getUserTel())) {
            updateList.add("userTel");
            account.updateUserTel(command.userTel());
        }
        if (command.isAddressUpdateRequired(account.getAddress())) {
            updateList.add("address");
            account.updateAddress(command.address());
        }

        if (updateList.isEmpty()) {
            return UpdateAccountServiceResponse.builder()
                .updateYn("N")
                .updateList(updateList)
                .build();
        }

        AccountHistory history = new AccountHistory()
            .createAccountHistoryForUpdate(accountId, String.join(",", updateList));

        registerAccountHistoryPort.register(history);
        updateAccountPort.update(account);

        return UpdateAccountServiceResponse.builder()
            .updateYn("Y")
            .updateList(updateList)
            .build();
    }
}
