package com.product.adapter.out.persistence.jpa.shard1;

import com.product.domain.model.AuthorizationRule;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "AUTHORIZATION_RULE")
@NoArgsConstructor
class AuthorizationRuleEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SORT_ORDER")
    private int sortOrder;

    @Column(name = "HTTP_METHOD")
    private String httpMethod;

    @Column(name = "URI_PATTERN")
    private String uriPattern;

    @Column(name = "ROLE")
    private String role;

    @Builder
    AuthorizationRuleEntity(Long id, int sortOrder, String httpMethod, String uriPattern,
        String role) {
        this.id = id;
        this.sortOrder = sortOrder;
        this.httpMethod = httpMethod;
        this.uriPattern = uriPattern;
        this.role = role;
    }

    AuthorizationRule toDomain() {
        return AuthorizationRule.builder()
            .httpMethod(httpMethod)
            .uriPattern(uriPattern)
            .role(role)
            .build();
    }

    static AuthorizationRuleEntity of(AuthorizationRule domain) {
        return AuthorizationRuleEntity.builder()
            .httpMethod(domain.httpMethod())
            .uriPattern(domain.uriPattern())
            .role(domain.role())
            .build();
    }
}
