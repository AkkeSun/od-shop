package com.product.adapter.in.register_product;

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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterProductRequest {

    @Size(max = 50, message = "상품명은 50자 이하여야 합니다", groups = SizeGroups.class)
    @NotBlank(message = "상품명은 필수값 입니다", groups = NotBlankGroups.class)
    private String productName;

    @ValidCategory(groups = CustomGroups.class)
    @NotBlank(message = "카테고리는 필수값 입니다", groups = SizeGroups.class)
    private String category;

    @Min(value = 1, message = "금액은 필수값 입니다", groups = SizeGroups.class)
    private long price;

    @Min(value = 20, message = "상품 수량은 20 이상이어야 합니다", groups = SizeGroups.class)
    private long quantity;

    @NotBlank(message = "상품 이미지는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 50, message = "상품 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    private String productImgUrl;

    @NotBlank(message = "상품 설명 이미지는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 50, message = "상품 설명 이미지는 50자 이하여야 합니다", groups = SizeGroups.class)
    private String descriptionImgUrl;

    @NotNull(message = "상품 옵션은 필수값 입니다.", groups = NotBlankGroups.class)
    private Set<String> productOption;

    @NotNull(message = "키워드는 필수값 입니다.", groups = NotBlankGroups.class)
    private Set<String> keywords;
    
    @Builder
    public RegisterProductRequest(String category, String descriptionImgUrl, Set<String> keywords,
        long price, String productImgUrl, String productName, Set<String> productOption,
        long quantity) {
        this.category = category;
        this.descriptionImgUrl = descriptionImgUrl;
        this.keywords = keywords;
        this.price = price;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.productOption = productOption;
        this.quantity = quantity;
    }

    RegisterProductCommand toCommand(Account account) {
        return RegisterProductCommand.builder()
            .account(account)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .descriptionImgUrl(descriptionImgUrl)
            .productOption(productOption)
            .keywords(keywords)
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
            return super.toString();
        }
    }
}
