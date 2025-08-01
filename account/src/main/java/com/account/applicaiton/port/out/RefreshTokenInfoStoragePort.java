package com.account.applicaiton.port.out;

import com.account.domain.model.RefreshTokenInfo;

public interface RefreshTokenInfoStoragePort {

    void registerToken(RefreshTokenInfo token);

    void deleteByEmail(String email);

    RefreshTokenInfo findByEmailAndUserAgent(String email, String userAgent);

    void updateToken(RefreshTokenInfo tokenCache);
}
