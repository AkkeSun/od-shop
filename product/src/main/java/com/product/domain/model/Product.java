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
    private long reservedQuantity;
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
        String deleteYn, double totalScore, LocalDateTime updateDateTime, long reservedQuantity) {
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
        this.reservedQuantity = reservedQuantity;
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
            .price(command.price())
            .quantity(command.quantity())
            .reservedQuantity(0)
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

    @JsonIgnore
    public void updateKeywords(String keyword) {
        this.keywords = Set.of(keyword.replace("\n", "").split(","));
    }

    @JsonIgnore
    public String getEmbeddingDocument() {
        return String.format("""
                    이 상품의 이름은 %s 이고, %s 카테고리에 속해 있습니다. \s
                    상품의 가격은 약 %d원입니다. \s
                    상품과 관련된 키워드는 %s 입니다.
                """,
            productName, category.description(), price, String.join(", ", keywords)
        );
    }

    @JsonIgnore
    public String getKeywordQueryDocument() {
        return String.format("""
                    너는 이커머스 상품 정보에서 고객의 검색 의도에 맞는 키워드를 추출하는 SEO 전문가야. \s
                    아래에 제공되는 '상품명', '상품 카테고리', '상품 옵션' 정보를 바탕으로, 고객들이 검색할 만한 핵심 키워드 10개를 추출해 줘. \s
                    \s
                    * 상품명: %s \s
                    * 상품 카테고리: %s \s
                    * 상품 옵션: 색상: %s \s
                                
                    결과는 키워드1, 키워드2, ... 형태로 출력해야 한다. \s
                    상품의 특징, 소재, 스타일, 타겟 고객, 사용 상황을 종합적으로 고려하고, 다른 설명은 절대 붙이지 마라. \s
                """,
            productName, category.description(), String.join(", ", productOption)
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
    public boolean isReservable() {
        return 0 < quantity - reservedQuantity;
    }

    @JsonIgnore
    public void reserve(long reservedQuantity) {
        this.reservedQuantity += reservedQuantity;
        this.updateDateTime = LocalDateTime.now();
    }

    @JsonIgnore
    public void confirmReservation(ProductReserveHistory reservation) {
        this.reservedQuantity -= reservation.reservedQuantity();
        this.quantity -= reservation.reservedQuantity();
        this.salesCount += reservation.reservedQuantity();
        this.updateDateTime = LocalDateTime.now();
    }

    @JsonIgnore
    public void cancelReservation(ProductReserveHistory reservation) {
        this.reservedQuantity -= reservation.reservedQuantity();
        this.updateDateTime = LocalDateTime.now();
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
