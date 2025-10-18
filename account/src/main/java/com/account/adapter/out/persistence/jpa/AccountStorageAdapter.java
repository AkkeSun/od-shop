package com.account.adapter.out.persistence.jpa;

import static com.common.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;

import com.account.applicaiton.port.out.AccountStoragePort;
import com.account.domain.model.Account;
import com.common.infrastructure.exception.CustomNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountStorageAdapter implements AccountStoragePort {

    private final PasswordEncoder encoder;
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
        AccountEntity entity = accountRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));

        if(!encoder.matches(password, entity.getPassword())) {
            throw new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO);
        }
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
    @Transactional
    public void update(Account account) {
        accountRepository.update(accountMapper.toEntity(account));
    }
}
