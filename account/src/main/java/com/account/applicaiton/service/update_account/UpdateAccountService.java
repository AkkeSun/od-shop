package com.account.applicaiton.service.update_account;

import static com.account.domain.model.AccountHistory.createAccountHistoryForUpdate;
import static com.account.infrastructure.util.JsonUtil.toJsonString;

import com.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.applicaiton.port.out.MessageProducerPort;
import com.account.domain.model.Account;
import com.account.domain.model.AccountHistory;
import com.account.infrastructure.util.JwtUtil;
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
    private final AccountStoragePort accountStoragePort;
    private final MessageProducerPort messageProducerPort;

    @Override
    public UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command) {
        Long accountId = jwtUtil.getAccountId(command.accessToken());
        Account account = accountStoragePort.findById(accountId);

        List<String> updateList = new ArrayList<>();
        if (command.isUsernameUpdateRequired(account.getUsername())) {
            updateList.add("username");
            account.updateUsername(command.username());
        }
        if (command.isPasswordUpdateRequired(account.getPassword())) {
            updateList.add("password");
            account.updatePassword(command.password());
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

        AccountHistory history = createAccountHistoryForUpdate(accountId,
            String.join(",", updateList));

        accountStoragePort.update(account);
        messageProducerPort.sendMessage("account-history", toJsonString(history));

        return UpdateAccountServiceResponse.builder()
            .updateYn("Y")
            .updateList(updateList)
            .build();
    }
}
