package com.account.applicaiton.port.in;

import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;

public interface DeleteTokenUseCase {
    DeleteTokenServiceResponse deleteToken(String accessToken);
}
