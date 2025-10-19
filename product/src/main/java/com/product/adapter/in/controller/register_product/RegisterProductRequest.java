package com.product.adapter.in.controller.register_product;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.Contains;
import com.common.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.common.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import com.product.application.port.in.command.RegisterProductCommand;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
record RegisterProductRequest(
    @Size(max = 50, message = "상품명은 50자 이하여야 합니다", groups = SizeGroups.class)
    @NotBlank(message = "상품명은 필수값 입니다", groups = NotBlankGroups.class)
    String productName,

    @Contains(
        values = {"DIGITAL", "FASHION", "SPORTS", "FOOD", "LIFE", "TOTAL"},
        groups = CustomGroups.class, message = "존재하지 않은 카테고리 입니다")
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

    RegisterProductCommand toCommand(LoginAccountInfo loginInfo) {
        return RegisterProductCommand.builder()
            .loginInfo(loginInfo)
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
        return toJsonString(this);
    }
}
