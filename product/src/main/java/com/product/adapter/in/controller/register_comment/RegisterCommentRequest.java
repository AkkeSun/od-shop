package com.product.adapter.in.controller.register_comment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.RegisterCommentCommand;
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
record RegisterCommentRequest(

    @NotBlank(message = "코멘트는 필수값 입니다.", groups = NotBlankGroups.class)
    @Size(min = 10, max = 50, message = "코멘트는 10자 이상 50자 이하여야 합니다.", groups = SizeGroups.class)
    String comment,

    @NotNull(message = "점수는 필수값 입니다.", groups = NotBlankGroups.class)
    @DecimalMin(value = "0.5", message = "점수는 0.5 이상 5.0 이하여야 합니다.", groups = SizeGroups.class)
    @DecimalMax(value = "5.0", message = "점수는 0.5 이상 5.0 이하여야 합니다.", groups = SizeGroups.class)
    Double score
) {

    RegisterCommentCommand toCommand(Long productId, Account account) {
        return RegisterCommentCommand.builder()
            .productId(productId)
            .account(account)
            .score(score)
            .comment(comment)
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
