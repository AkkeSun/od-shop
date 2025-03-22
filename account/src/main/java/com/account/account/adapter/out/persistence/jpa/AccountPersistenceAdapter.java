package com.account.account.adapter.out.persistence.jpa;

import static com.account.global.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;

import com.account.account.applicaiton.port.out.DeleteAccountPort;
import com.account.account.applicaiton.port.out.FindAccountPort;
import com.account.account.applicaiton.port.out.RegisterAccountPort;
import com.account.account.applicaiton.port.out.UpdateAccountPort;
import com.account.account.domain.model.Account;
import com.account.global.exception.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
class AccountPersistenceAdapter implements FindAccountPort, RegisterAccountPort, DeleteAccountPort,
    UpdateAccountPort {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;

    @Override
    public Account findById(Long id) {
        AccountEntity entity = accountRepository.findById(id)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public Account findByEmail(String email) {
        AccountEntity entity = accountRepository.findByEmail(email)
            .orElseThrow(() -> new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));
        return accountMapper.toDomain(entity);
    }

    @Override
    public Account findByEmailAndPassword(String email, String password) {
        AccountEntity entity = accountRepository.findByEmailAndPassword(email, password)
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
