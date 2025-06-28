package com.product.adapter.out.client.elasticsearch;

import static com.product.infrastructure.util.DateUtil.parseDateTime;

import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import java.util.List;
import java.util.Set;
import lombok.Builder;

@Builder
public record FindProductsEsResponse(
    int took,
    boolean timed_out,
    Shards _shards,
    HitsWrapper hits
) {

    @Builder
    public record Shards(
        int total,
        int successful,
        int skipped,
        int failed
    ) {

    }

    @Builder
    public record HitsWrapper(
        Total total,
        Double max_score,
        List<Hit> hits
    ) {

    }

    @Builder
    public record Total(
        int value,
        String relation
    ) {

    }

    @Builder
    public record Hit(
        String _index,
        String _id,
        Double _score,
        DocumentSource _source
    ) {

        @Builder
        public record DocumentSource(
            String _class,
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

            Product toDomain() {
                return Product.builder()
                    .id(productId)
                    .productName(productName)
                    .keywords(Set.of(keywords.split(",")))
                    .sellerEmail(sellerEmail)
                    .productImgUrl(productImgUrl)
                    .price(price)
                    .salesCount(salesCount)
                    .reviewCount(reviewCount)
                    .totalScore(totalScore)
                    .category(Category.valueOf(category))
                    .regDateTime(parseDateTime(regDateTime))
                    .build();
            }

            ProductRecommend toRecommend() {
                return ProductRecommend.builder()
                    .id(productId)
                    .productName(productName)
                    .productImgUrl(productImgUrl)
                    .price(price)
                    .build();
            }
        }
    }

    boolean isEmpty() {
        return hits.total.value == 0;
    }

}
