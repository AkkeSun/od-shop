package com.product.adapter.in.register_product;

import com.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.domain.model.Category;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterProductResponse {

    private Long productId;
    private String sellerEmail;
    private String productName;
    private String productImgUrl;
    private String descriptionImgUrl;
    private Set<String> keywords;
    private Set<String> productOption;
    private long price;
    private long quantity;
    private Category category;

    @Builder
    RegisterProductResponse(Long productId, String sellerEmail, String productName,
        String productImgUrl, Set<String> keywords, Set<String> productOption,
        String descriptionImgUrl,
        long price, long quantity, Category category) {
        this.productId = productId;
        this.sellerEmail = sellerEmail;
        this.productName = productName;
        this.productImgUrl = productImgUrl;
        this.keywords = keywords;
        this.productOption = productOption;
        this.descriptionImgUrl = descriptionImgUrl;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
    }

    RegisterProductResponse of(RegisterProductServiceResponse serviceResponse) {
        return RegisterProductResponse.builder()
            .productId(serviceResponse.productId())
            .sellerEmail(serviceResponse.sellerEmail())
            .productName(serviceResponse.productName())
            .productImgUrl(serviceResponse.productImgUrl())
            .descriptionImgUrl(serviceResponse.descriptionImgUrl())
            .keywords(serviceResponse.keywords())
            .productOption(serviceResponse.productOption())
            .price(serviceResponse.price())
            .quantity(serviceResponse.quantity())
            .category(serviceResponse.category())
            .build();
    }
}
