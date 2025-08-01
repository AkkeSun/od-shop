package com.account.applicaiton.port.in;

import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;

public interface UpdateTokenUseCase {

    UpdateTokenServiceResponse update(String refreshToken);
}
