package com.account.domain.model;

import static com.account.infrastructure.util.DateUtil.getCurrentDate;

import com.account.applicaiton.port.in.command.RegisterAccountCommand;
import com.account.applicaiton.port.in.command.UpdateAccountCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class Account {

    private Long id;

    private String email;

    private String password;

    private String username;

    private String userTel;

    private String address;

    private List<Role> roles;

    private LocalDateTime regDateTime;

    private String regDate;

    @Builder
    public Account(Long id, String email, String password, String username, String userTel,
        String address, List<Role> roles, LocalDateTime regDateTime, String regDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.roles = roles;
        this.regDateTime = regDateTime;
        this.regDate = regDate;
    }

    public static Account of(Claims claims) {
        return Account.builder()
            .email(claims.getSubject())
            .id(Long.valueOf(claims.get("accountId").toString()))
            .roles(Arrays.stream(claims.get("roles").toString().split(","))
                .map(role -> Role.builder().name(role).build())
                .toList())
            .build();
    }

    public static Account of(RegisterAccountCommand command, Map<String, Role> validRoles) {
        return Account.builder()
            .email(command.email())
            .password(command.password())
            .username(command.username())
            .userTel(command.userTel())
            .address(command.address())
            .roles(command.roles().stream()
                .map(validRoles::get)
                .toList())
            .regDateTime(LocalDateTime.now())
            .regDate(getCurrentDate())
            .build();
    }

    @JsonIgnore
    public List<String> update(UpdateAccountCommand command) {
        List<String> updateList = new java.util.ArrayList<>();
        if (isUsernameUpdateRequired(command.username())) {
            updateList.add("username");
            this.username = command.username();
        }
        if (isPasswordUpdateRequired(command.password())) {
            updateList.add("password");
            this.password = command.password();
        }
        if (isUserTelUpdateRequired(command.userTel())) {
            updateList.add("userTel");
            this.userTel = command.userTel();
        }
        if (isAddressUpdateRequired(command.address())) {
            updateList.add("address");
            this.address = command.address();
        }
        return updateList;
    }

    @JsonIgnore
    private boolean isUsernameUpdateRequired(String newUsername) {
        return StringUtils.hasText(newUsername) && !newUsername.equals(this.username);
    }

    @JsonIgnore
    private boolean isPasswordUpdateRequired(String newPassword) {
        return StringUtils.hasText(newPassword) && !newPassword.equals(this.password);
    }

    @JsonIgnore
    private boolean isUserTelUpdateRequired(String newUserTel) {
        return StringUtils.hasText(newUserTel) && !newUserTel.equals(this.userTel);
    }

    @JsonIgnore
    private boolean isAddressUpdateRequired(String newAddress) {
        return StringUtils.hasText(newAddress) && !newAddress.equals(this.address);
    }
}
