package com.account.adapter.out.persistence;

import static com.account.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;

import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.util.AesUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class AccountPersistenceAdapter implements AccountStoragePort {

    private final AesUtil aesUtil;
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Override
    public Account findById(Long id) {
        AccountEntity entity = accountRepository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) {
        AccountEntity entity = accountRepository.findByEmailAndPassword(email,
                aesUtil.encryptText(password))
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }

    @Override
    public Account register(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        AccountEntity savedEntity = accountRepository.save(entity);
        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public void deleteById(Long id) {
        accountRepository.deleteById(id);
    }

    @Override
    @Transactional // for test
    public void update(Account account) {
        accountRepository.update(accountMapper.toEntity(account));
    }
}
