package com.product.adapter.in.controller.register_review;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import com.product.application.port.in.command.RegisterReviewCommand;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
record RegisterReviewRequest(

    @NotBlank(message = "리뷰는 필수값 입니다.", groups = NotBlankGroups.class)
    @Size(min = 10, max = 50, message = "리뷰는 10자 이상 50자 이하여야 합니다.", groups = SizeGroups.class)
    String review,

    @NotNull(message = "점수는 필수값 입니다.", groups = NotBlankGroups.class)
    @DecimalMin(value = "0.5", message = "점수는 0.5 이상 5.0 이하여야 합니다.", groups = SizeGroups.class)
    @DecimalMax(value = "5.0", message = "점수는 0.5 이상 5.0 이하여야 합니다.", groups = SizeGroups.class)
    Double score
) {

    RegisterReviewCommand toCommand(Long productId, LoginAccountInfo loginInfo) {
        return RegisterReviewCommand.builder()
            .productId(productId)
            .loginInfo(loginInfo)
            .score(score)
            .review(review)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
