package com.product.adapter.in.controller.register_review;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.RegisterReviewCommand;
import com.product.domain.model.Account;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
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

    RegisterReviewCommand toCommand(Long productId, Account account) {
        return RegisterReviewCommand.builder()
            .productId(productId)
            .account(account)
            .score(score)
            .review(review)
            .build();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
