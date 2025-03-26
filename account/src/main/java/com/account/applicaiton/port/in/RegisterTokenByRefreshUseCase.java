package com.account.applicaiton.port.in;

import com.account.applicaiton.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;

public interface RegisterTokenByRefreshUseCase {

    RegisterTokenByRefreshServiceResponse registerTokenByRefresh(
        String refreshToken);
}
