package com.account.applicaiton.service.delete_token;

import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.port.out.RedisStoragePort;
import com.account.applicaiton.port.out.TokenStoragePort;
import com.account.domain.model.Account;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class DeleteTokenService implements DeleteTokenUseCase {

    @Value("${spring.data.redis.key.token}")
    private String tokenRedisKey;
    private final RedisStoragePort redisStoragePort;
    private final TokenStoragePort tokenStoragePort;

    @Override
    public DeleteTokenServiceResponse deleteToken(Account account) {
        List<String> keys = redisStoragePort.getKeys(
            String.format(tokenRedisKey, account.getEmail(), "*"));

        redisStoragePort.delete(keys);
        tokenStoragePort.deleteByEmail(account.getEmail());

        log.info("[Delete token] {}", account.getEmail());
        return DeleteTokenServiceResponse.ofSuccess();
    }
}
