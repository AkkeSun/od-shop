package com.account.applicaiton.port.in;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;

public interface UpdateAccountUseCase {

    UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command);
}
