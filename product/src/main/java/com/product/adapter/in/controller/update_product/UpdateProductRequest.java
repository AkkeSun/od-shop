package com.product.adapter.in.controller.update_product;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.groups.ValidationGroups.SizeGroups;
import com.product.application.port.in.command.UpdateProductCommand;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Builder;

@Builder
record UpdateProductRequest(

    long price,

    Set<String> productOption,

    Set<String> keywords,

    @Size(max = 50, message = "상품명은 50자 이하여야 합니다", groups = SizeGroups.class)
    String productName,

    @Size(max = 50, message = "상품 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    String productImgUrl,

    @Size(max = 50, message = "상품 설명 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    String descriptionImgUrl
) {

    UpdateProductCommand toCommand(LoginAccountInfo loginInfo, Long productId) {
        return UpdateProductCommand.builder()
            .loginInfo(loginInfo)
            .productId(productId)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .descriptionImgUrl(descriptionImgUrl)
            .keywords(keywords)
            .price(price)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
