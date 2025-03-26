package com.account.applicaiton.port.in;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;

public interface RegisterAccountUseCase {

    RegisterAccountServiceResponse registerAccount(RegisterAccountCommand command);
}
