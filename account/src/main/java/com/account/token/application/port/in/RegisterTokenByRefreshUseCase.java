package com.account.token.application.port.in;

import com.account.token.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;

public interface RegisterTokenByRefreshUseCase {

    RegisterTokenByRefreshServiceResponse registerTokenByRefresh(
        String refreshToken);
}
