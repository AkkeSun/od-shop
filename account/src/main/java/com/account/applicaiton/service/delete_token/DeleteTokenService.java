package com.account.applicaiton.service.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.infrastructure.properties.RedisProperties;
import com.common.infrastructure.resolver.LoginAccountInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    private final RedisProperties redisProperties;
    private final RedisStoragePort redisStoragePort;

    @Override
    public DeleteTokenServiceResponse deleteToken(LoginAccountInfo account) {
        List<String> keys = redisStoragePort.getKeys(
            String.format(redisProperties.key().token(), account.getEmail(), "*"));

        redisStoragePort.delete(keys);

        log.info("[Delete token] {}", account.getEmail());
        return DeleteTokenServiceResponse.ofSuccess();
    }
}
