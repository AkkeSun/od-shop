package com.account.token.application.port.in;

import com.account.token.application.port.in.command.RegisterTokenCommand;
import com.account.token.application.service.register_token.RegisterTokenServiceResponse;

public interface RegisterTokenUseCase {

    RegisterTokenServiceResponse registerToken(RegisterTokenCommand command);
}
