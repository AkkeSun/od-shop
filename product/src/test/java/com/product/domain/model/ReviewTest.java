package com.product.domain.model;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.product.application.port.in.command.RegisterReviewCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReviewTest {

    @Nested
    @DisplayName("[of] 도메인을 생성하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 도메인 객체를 생성하는지 확인한다.")
        void success() {
            // given
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(10L)
                .loginInfo(LoginAccountInfo.builder().id(1L).email("od").build())
                .score(5.0)
                .review("This is a test comment")
                .build();
            Long id = 1L;

            // when
            Review review = Review.of(command, id);

            // then
            assert review.id().equals(id);
            assert review.productId().equals(command.productId());
            assert review.customerId().equals(command.loginInfo().getId());
            assert review.customerEmail().equals(command.loginInfo().getEmail());
            assert review.score() == review.score();
            assert review.review().equals(command.review());
            assert review.regDate() != null;
            assert review.regDateTime() != null;
        }
    }
}