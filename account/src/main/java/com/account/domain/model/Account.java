package com.account.domain.model;

import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Account {

    private Long id;

    private String email;

    private String password;

    private String username;

    private String userTel;

    private String address;

    private Role role;

    private LocalDateTime regDateTime;

    private String regDate;

    @Builder
    public Account(Long id, String email, String password, String username, String userTel,
        String address, Role role, LocalDateTime regDateTime, String regDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
        this.regDateTime = regDateTime;
        this.regDate = regDate;
    }

    public static Account of(Claims claims) {
        return Account.builder()
            .email(claims.getSubject())
            .id(Long.valueOf(claims.get("accountId").toString()))
            .role(Role.valueOf(claims.get("role").toString()))
            .build();
    }

    public List<String> update(UpdateAccountCommand command) {
        List<String> updateList = new java.util.ArrayList<>();
        if (command.isUsernameUpdateRequired(this.username)) {
            updateList.add("username");
            this.username = command.username();
        }
        if (command.isPasswordUpdateRequired(this.password)) {
            updateList.add("password");
            this.password = command.password();
        }
        if (command.isUserTelUpdateRequired(this.userTel)) {
            updateList.add("userTel");
            this.userTel = command.userTel();
        }
        if (command.isAddressUpdateRequired(this.address)) {
            updateList.add("address");
            this.address = command.address();
        }
        return updateList;
    }
}
