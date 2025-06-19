package com.product.adapter.in.update_product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.application.port.in.command.UpdateProductCommand;
import com.product.domain.model.Account;
import com.product.infrastructure.validation.groups.ValidationGroups.SizeGroups;
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

    UpdateProductCommand toCommand(Account account, Long productId) {
        return UpdateProductCommand.builder()
            .account(account)
            .productId(productId)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .descriptionImgUrl(descriptionImgUrl)
            .productOption(productOption)
            .keywords(keywords)
            .price(price)
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
