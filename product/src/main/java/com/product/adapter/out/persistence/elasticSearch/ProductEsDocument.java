package com.product.adapter.out.persistence.elasticSearch;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@Document(indexName = "product")
class ProductEsDocument {

    @Id
    private Long productId;

    @Field(type = FieldType.Text, analyzer = "korean") // nori analyzer for Korean text
    private String productName;

    @Field(type = FieldType.Text, analyzer = "korean") // nori analyzer for Korean text
    private String keywords;

    @Field(type = FieldType.Text, index = false)
    private String sellerEmail;

    @Field(type = FieldType.Text, index = false)
    private String productImgUrl;

    @Field(type = FieldType.Long, index = false)
    private long price;

    @Field(type = FieldType.Long, index = false)
    private long salesCount;

    @Field(type = FieldType.Long, index = false)
    private long reviewCount;

    @Field(type = FieldType.Double, index = false)
    private double totalScore;

    @Field(type = FieldType.Dense_Vector, dims = 384) // vector indexing
    private float[] embedding;

    @Field(type = FieldType.Keyword) // 문자열 그대로 인덱싱
    private Category category;

    @Field(type = FieldType.Date, format = {}, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime regDateTime;

    @Builder
    public ProductEsDocument(Category category, float[] embedding, String keywords, long price,
        Long productId, String productImgUrl, String productName, LocalDateTime regDateTime,
        long reviewCount, long salesCount, String sellerEmail, double totalScore) {
        this.category = category;
        this.embedding = embedding;
        this.keywords = keywords;
        this.price = price;
        this.productId = productId;
        this.productImgUrl = productImgUrl;
        this.productName = productName;
        this.regDateTime = regDateTime;
        this.reviewCount = reviewCount;
        this.salesCount = salesCount;
        this.sellerEmail = sellerEmail;
        this.totalScore = totalScore;
    }

    static ProductEsDocument of(Product product, float[] embedding) {
        return ProductEsDocument.builder()
            .productId(product.getId())
            .sellerEmail(product.getSellerEmail())
            .productName(product.getProductName())
            .productImgUrl(product.getProductImgUrl())
            .price(product.getPrice())
            .keywords(String.join(",", product.getKeywords()))
            .salesCount(product.getSalesCount())
            .reviewCount(product.getReviewCount())
            .totalScore(product.getTotalScore())
            .embedding(embedding)
            .category(product.getCategory())
            .regDateTime(product.getRegDateTime())
            .build();
    }

    Product toDomain() {
        return Product.builder()
            .id(productId)
            .sellerEmail(sellerEmail)
            .productName(productName)
            .productImgUrl(productImgUrl)
            .price(price)
            .keywords(Arrays.stream(keywords.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet()))
            .salesCount(salesCount)
            .reviewCount(reviewCount)
            .totalScore(totalScore)
            .category(category)
            .regDateTime(regDateTime)
            .build();
    }
}
