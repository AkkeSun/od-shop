package com.account.adapter.out.persistence.jpa;

import com.account.domain.model.RefreshTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RefreshTokenInfoMapperTest {

    RefreshTokenInfoMapper mapper = new RefreshTokenInfoMapper();

    @Nested
    @DisplayName("[toDomain] 엔티티 정보를 도메인 정보로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[success] 엔티티 정보를 도메인 정보로 정상적으로 변환하는지 확인한다.")
        void success() {
            // given
            RefreshTokenInfoEntity entity = RefreshTokenInfoEntity.builder()
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .userAgent("userAgent")
                .build();

            // when
            RefreshTokenInfo domain = mapper.toDomain(entity);

            // then
            assert domain.getRefreshToken().equals("refreshToken");
            assert domain.getRegDateTime().equals("regDateTime");
            assert domain.getUserAgent().equals("userAgent");
        }
    }

    @Nested
    @DisplayName("[toEntity] 도메인 정보를 엔티티 정보로 변환하는 메소드")
    class Describe_toEntity {

        @Test
        @DisplayName("[success] 도메인 정보를 엔티티 정보로 정상적으로 변환하는지 확인한다.")
        void success() {
            // given
            RefreshTokenInfo domain = RefreshTokenInfo.builder()
                .refreshToken("refreshToken")
                .regDateTime("regDateTime")
                .userAgent("userAgent")
                .build();

            // when
            RefreshTokenInfoEntity entity = mapper.toEntity(domain);

            // then
            assert entity.getRefreshToken().equals("refreshToken");
            assert entity.getRegDateTime().equals("regDateTime");
            assert entity.getUserAgent().equals("userAgent");
        }
    }
}