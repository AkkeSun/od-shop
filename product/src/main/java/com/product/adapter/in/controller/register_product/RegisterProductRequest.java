package com.product.adapter.in.controller.register_product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.RegisterProductCommand;
import com.product.domain.model.Account;
import com.product.infrastructure.validation.ValidCategory;
import com.product.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
record RegisterProductRequest(
    @Size(max = 50, message = "상품명은 50자 이하여야 합니다", groups = SizeGroups.class)
    @NotBlank(message = "상품명은 필수값 입니다", groups = NotBlankGroups.class)
    String productName,

    @ValidCategory(groups = CustomGroups.class)
    @NotBlank(message = "카테고리는 필수값 입니다", groups = SizeGroups.class)
    String category,

    @Min(value = 1, message = "금액은 필수값 입니다", groups = SizeGroups.class)
    long price,

    @Min(value = 20, message = "상품 수량은 20 이상이어야 합니다", groups = SizeGroups.class)
    long quantity,

    @NotBlank(message = "상품 이미지는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 50, message = "상품 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    String productImgUrl,

    @NotBlank(message = "상품 설명 이미지는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 50, message = "상품 설명 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    String descriptionImgUrl
) {

    RegisterProductCommand toCommand(Account account) {
        return RegisterProductCommand.builder()
            .account(account)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .descriptionImgUrl(descriptionImgUrl)
            .price(price)
            .quantity(quantity)
            .category(category)
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
