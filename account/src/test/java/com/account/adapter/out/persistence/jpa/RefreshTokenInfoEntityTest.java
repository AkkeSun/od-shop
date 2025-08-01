package com.account.adapter.out.persistence.jpa;

import com.account.domain.model.RefreshTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RefreshTokenInfoEntityTest {

    @Nested
    @DisplayName("[updateByDomain] 도메인으로 엔티티 엔티티 정보를 수정하는 메소드")
    class Describe_updateByDomain {

        @Test
        @DisplayName("[success] 도메인 정보를 엔티티 정보를 정상적으로 수정하는지 확인한다.")
        void success() {
            // given
            RefreshTokenInfoEntity entity = RefreshTokenInfoEntity.builder()
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .userAgent("userAgent")
                .build();
            RefreshTokenInfo domain = RefreshTokenInfo.builder()
                .refreshToken("newRefreshToken")
                .regDateTime("newRegDateTime")
                .userAgent("newUserAgent")
                .build();

            // when
            entity.updateByDomain(domain);

            // then
            assert entity.getRefreshToken().equals("newRefreshToken");
            assert entity.getRegDateTime().equals("newRegDateTime");
            assert entity.getUserAgent().equals("newUserAgent");
        }
    }
}