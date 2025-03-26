package com.account.applicaiton.port.in;

import com.account.applicaiton.port.in.command.RegisterTokenCommand;
import com.account.applicaiton.service.register_token.RegisterTokenServiceResponse;

public interface RegisterTokenUseCase {

    RegisterTokenServiceResponse registerToken(RegisterTokenCommand command);
}
