package com.product.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Product {

    private Long productId;
    private long sellerId;
    private String sellerEmail;
    private String productName;
    private String productImg;
    private List<String> productOption;
    private String description;
    private long price;
    private long quantity;
    private Category category;
    private String regDate;
    private LocalDateTime regDateTime;

    @Builder
    public Product(Category category, String description, long price, Long productId,
        String productImg,
        String productName, List<String> productOption, long quantity, String regDate,
        LocalDateTime regDateTime, String sellerEmail, long sellerId) {
        this.category = category;
        this.description = description;
        this.price = price;
        this.productId = productId;
        this.productImg = productImg;
        this.productName = productName;
        this.productOption = productOption;
        this.quantity = quantity;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.sellerEmail = sellerEmail;
        this.sellerId = sellerId;
    }
}
