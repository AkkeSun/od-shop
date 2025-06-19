package com.product.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.product.application.port.in.command.RegisterProductCommand;
import com.product.application.port.in.command.UpdateProductCommand;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
public class Product {

    private Long id;
    private Long sellerId;
    private String sellerEmail;
    private String productName;
    private String productImgUrl;
    private String descriptionImgUrl;
    private Set<String> productOption;
    private Set<String> keywords;
    private long price;
    private long quantity;
    private Category category;
    private String deleteYn;
    private LocalDate regDate;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;

    // product metric
    private long salesCount;
    private long reviewCount;
    private long hitCount;
    private double reviewScore;
    private double totalScore;
    private boolean needsEsUpdate;

    @Builder
    public Product(Category category, String descriptionImgUrl, long hitCount, Set<String> keywords,
        boolean needsEsUpdate, long price, Long id, String productImgUrl, String productName,
        Set<String> productOption, long quantity, LocalDate regDate, LocalDateTime regDateTime,
        long reviewCount, double reviewScore, long salesCount, String sellerEmail, Long sellerId,
        String deleteYn, double totalScore, LocalDateTime updateDateTime) {
        this.category = category;
        this.descriptionImgUrl = descriptionImgUrl;
        this.hitCount = hitCount;
        this.keywords = keywords;
        this.needsEsUpdate = needsEsUpdate;
        this.price = price;
        this.id = id;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.productOption = productOption;
        this.quantity = quantity;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.salesCount = salesCount;
        this.sellerEmail = sellerEmail;
        this.sellerId = sellerId;
        this.deleteYn = deleteYn;
        this.totalScore = totalScore;
        this.updateDateTime = updateDateTime;
    }

    public static Product of(RegisterProductCommand command, Long id) {
        return Product.builder()
            .id(id)
            .sellerId(command.account().id())
            .sellerEmail(command.account().email())
            .productName(command.productName())
            .productImgUrl(command.productImgUrl())
            .descriptionImgUrl(command.descriptionImgUrl())
            .productOption(command.productOption())
            .keywords(command.keywords())
            .price(command.price())
            .quantity(command.quantity())
            .hitCount(0)
            .needsEsUpdate(false)
            .salesCount(0)
            .reviewCount(0)
            .reviewScore(0)
            .totalScore(0)
            .category(Category.valueOf(command.category()))
            .deleteYn("N")
            .regDate(LocalDate.now())
            .regDateTime(LocalDateTime.now())
            .build();
    }

    public String getEmbeddingDocument() {
        String keywordsString = String.join(", ", keywords);
        return String.format("""
                    이 상품의 이름은 %s 이고, %s 카테고리에 속해 있습니다. \s
                    상품의 가격은 약 %d원입니다. \s
                    상품과 관련된 키워드는 %s 입니다.
                """,
            productName, category, price, keywordsString
        );
    }

    @JsonIgnore
    public boolean isSeller(Long accountId) {
        return this.sellerId.equals(accountId);
    }

    @JsonIgnore
    public List<String> update(UpdateProductCommand command) {
        List<String> updateList = new ArrayList<>();
        if (isProductNameRequired(command.productName())) {
            this.productName = command.productName();
            updateList.add("productName");
        }
        if (isProductImgUrlRequired(command.productImgUrl())) {
            this.productImgUrl = command.productImgUrl();
            updateList.add("productImgUrl");
        }
        if (isDescriptionImgUrlRequired(command.descriptionImgUrl())) {
            this.descriptionImgUrl = command.descriptionImgUrl();
            updateList.add("descriptionImgUrl");
        }
        if (isProductOptionRequired(command.productOption())) {
            this.productOption = command.productOption();
            updateList.add("productOption");
        }
        if (isKeywordsRequired(command.keywords())) {
            this.keywords = command.keywords();
            updateList.add("keywords");
        }
        if (isPriceRequired(command.price())) {
            this.price = command.price();
            updateList.add("price");
        }

        this.updateDateTime = LocalDateTime.now();
        this.needsEsUpdate = true;
        return updateList;
    }

    @JsonIgnore
    private boolean isProductNameRequired(String newProductName) {
        return StringUtils.hasText(newProductName) && !newProductName.equals(this.productName);
    }

    @JsonIgnore
    private boolean isProductImgUrlRequired(String newProductImgUrl) {
        return StringUtils.hasText(newProductImgUrl) && !newProductImgUrl.equals(
            this.productImgUrl);
    }

    @JsonIgnore
    private boolean isDescriptionImgUrlRequired(String newDescriptionImgUrl) {
        return StringUtils.hasText(newDescriptionImgUrl) && !newDescriptionImgUrl.equals(
            this.descriptionImgUrl);
    }

    @JsonIgnore
    private boolean isProductOptionRequired(Set<String> newProductOption) {
        if (newProductOption == null) {
            return false;
        }
        return !(this.productOption.containsAll(newProductOption) &&
            newProductOption.containsAll(this.productOption));
    }

    @JsonIgnore
    private boolean isKeywordsRequired(Set<String> newKeywords) {
        if (newKeywords == null) {
            return false;
        }
        return !(this.keywords.containsAll(newKeywords) &&
            newKeywords.containsAll(this.keywords));
    }

    @JsonIgnore
    private boolean isPriceRequired(long newPrice) {
        return newPrice > 0 && newPrice != this.price;
    }
}
