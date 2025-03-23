package com.account.token.application.port.in;

import com.account.token.application.service.delete_token.DeleteTokenServiceResponse;

public interface DeleteTokenUseCase {
    DeleteTokenServiceResponse deleteToken(String accessToken);
}
