package com.product.adapter.out.persistence.jpa.shard1;


import com.product.domain.model.Category;
import com.product.domain.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
class ProductShard1Entity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "SELLER_ID")
    private Long sellerId;

    @Column(name = "SELLER_EMAIL")
    private String sellerEmail;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_IMG")
    private String productImgUrl;

    @Column(name = "PRODUCT_OPTION")
    private String productOption;

    @Column(name = "KEYWORD")
    private String keyword;

    @Column(name = "DESCRIPTION")
    private String descriptionImgUrl;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "QUANTITY")
    private long quantity;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "DELETE_YN")
    private String deleteYn;

    @Column(name = "REG_DATE")
    private LocalDate regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Column(name = "UPDATE_DATE_TIME")
    private LocalDateTime updateDateTime;

    @Builder
    ProductShard1Entity(Category category, String descriptionImgUrl, String keyword,
        long price, Long id, String productImgUrl,
        String productName, String productOption, long quantity, LocalDate regDate,
        LocalDateTime regDateTime, String sellerEmail, Long sellerId, String deleteYn,
        LocalDateTime updateDateTime) {
        this.category = category;
        this.descriptionImgUrl = descriptionImgUrl;
        this.keyword = keyword;
        this.price = price;
        this.id = id;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.productOption = productOption;
        this.quantity = quantity;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.sellerEmail = sellerEmail;
        this.sellerId = sellerId;
        this.deleteYn = deleteYn;
        this.updateDateTime = updateDateTime;
    }

    public static ProductShard1Entity of(Product domain) {
        return ProductShard1Entity.builder()
            .id(domain.getId())
            .sellerId(domain.getSellerId())
            .sellerEmail(domain.getSellerEmail())
            .productName(domain.getProductName())
            .productImgUrl(domain.getProductImgUrl())
            .productOption(String.join(",", domain.getProductOption()))
            .keyword(String.join(",", domain.getKeywords()))
            .descriptionImgUrl(domain.getDescriptionImgUrl())
            .price(domain.getPrice())
            .quantity(domain.getQuantity())
            .category(domain.getCategory())
            .deleteYn(domain.getDeleteYn())
            .regDate(domain.getRegDate())
            .regDateTime(domain.getRegDateTime())
            .updateDateTime(domain.getUpdateDateTime())
            .build();
    }

    public Product toDomain() {
        return Product.builder()
            .id(id)
            .sellerId(sellerId)
            .sellerEmail(sellerEmail)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .productOption(new HashSet<>(List.of(productOption.split(","))))
            .keywords(new HashSet<>(List.of(keyword.split(","))))
            .descriptionImgUrl(descriptionImgUrl)
            .price(price)
            .quantity(quantity)
            .category(category)
            .deleteYn(deleteYn)
            .regDate(regDate)
            .regDateTime(regDateTime)
            .updateDateTime(updateDateTime)
            .build();
    }
}
