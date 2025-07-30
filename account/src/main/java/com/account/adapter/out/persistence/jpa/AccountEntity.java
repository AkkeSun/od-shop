package com.account.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ACCOUNT")
@NoArgsConstructor
class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_TEL")
    private String userTel;

    @Column(name = "ADDRESS")
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "ACCOUNT_ROLE",
        joinColumns = @JoinColumn(name = "ACCOUNT_ID"),
        inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Column(name = "REG_DATE")
    private String regDate;

    @Builder
    public AccountEntity(Long id, String email, String password, String username, String userTel,
        String address, Set<RoleEntity> roles, LocalDateTime regDateTime, String regDate) {
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
}
