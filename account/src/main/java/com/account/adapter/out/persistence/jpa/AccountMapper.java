package com.account.adapter.out.persistence.jpa;

import com.account.domain.model.Account;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class AccountMapper {

    private final PasswordEncoder encoder;

    public Account toDomain(AccountEntity entity) {
        return Account.builder()
            .id(entity.getId())
            .email(entity.getEmail())
            .password(entity.getPassword())
            .username(entity.getUsername())
            .userTel(entity.getUserTel())
            .address(entity.getAddress())
            .roles(entity.getRoles().stream()
                .map(RoleEntity::toDomain)
                .toList())
            .regDateTime(entity.getRegDateTime())
            .regDate(entity.getRegDate())
            .build();
    }

    public AccountEntity toEntity(Account domain) {
        return AccountEntity.builder()
            .id(domain.getId())
            .email(domain.getEmail())
            .password(encoder.encode(domain.getPassword()))
            .username(domain.getUsername())
            .userTel(domain.getUserTel())
            .address(domain.getAddress())
            .roles(domain.getRoles().stream()
                .map(RoleEntity::of)
                .collect(Collectors.toSet()))
            .regDateTime(domain.getRegDateTime())
            .regDate(domain.getRegDate())
            .build();
    }
}
