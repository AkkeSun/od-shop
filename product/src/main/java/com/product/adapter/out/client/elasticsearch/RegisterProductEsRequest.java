package com.product.adapter.out.client.elasticsearch;

import com.product.domain.model.Product;
import java.time.format.DateTimeFormatter;
import lombok.Builder;

@Builder
public record RegisterProductEsRequest(
    Long productId,
    String productName,
    String keywords,
    String sellerEmail,
    String productImgUrl,
    long price,
    long salesCount,
    long reviewCount,
    double totalScore,
    String category,
    String regDateTime,
    float[] embedding
) {

    static RegisterProductEsRequest of(Product product, float[] embedding) {
        return RegisterProductEsRequest.builder()
            .productId(product.getId())
            .productName(product.getProductName())
            .keywords(String.join(",", product.getKeywords()))
            .sellerEmail(product.getSellerEmail())
            .productImgUrl(product.getProductImgUrl())
            .price(product.getPrice())
            .salesCount(product.getSalesCount())
            .reviewCount(product.getReviewCount())
            .totalScore(product.getTotalScore())
            .category(product.getCategory().name())
            .regDateTime(product.getRegDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .embedding(embedding)
            .build();

    }
}
