package com.product.application.service.find_recommend_product;

import static com.product.domain.model.RecommendType.PERSONAL;
import static com.product.domain.model.RecommendType.POPULAR;
import static com.product.domain.model.RecommendType.TREND;
import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.FindRecommendProductUseCase;
import com.product.application.port.in.command.FindRecommendProductCommand;
import com.product.application.port.out.ElasticSearchClientPort;
import com.product.application.port.out.OrderClientPort;
import com.product.application.port.out.ProductStoragePort;
import com.product.application.port.out.RecommendStoragePort;
import com.product.application.port.out.RedisStoragePort;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import com.product.domain.model.RecommendType;
import com.product.infrastructure.util.EmbeddingUtil;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindRecommendProductService implements FindRecommendProductUseCase {

    @Value("${spring.data.redis.key.product-recommend}")
    private String productRecommendKey;

    @Value("${spring.data.redis.key.personal-recommend}")
    private String personalRecommendKey;

    @Value("${spring.data.redis.ttl.product-recommend}")
    private long productRecommendTtl;

    @Value("${spring.data.redis.ttl.personal-recommend}")
    private long personalRecommendTtl;

    private final EmbeddingUtil embeddingUtil;

    private final OrderClientPort orderClientPort;

    private final RedisStoragePort redisStoragePort;

    private final ProductStoragePort productStoragePort;

    private final RecommendStoragePort recommendStoragePort;

    private final ElasticSearchClientPort elasticSearchClientPort;

    @NewSpan
    @Override
    public FindRecommendProductServiceResponse findRecommendProductList(
        FindRecommendProductCommand command) {

        List<ProductRecommend> personalProducts = getRecommendProduct(command, PERSONAL);
        List<ProductRecommend> popularProducts = getRecommendProduct(command, POPULAR);
        List<ProductRecommend> trendProducts = getRecommendProduct(command, TREND);

        List<Long> excludedProductIds = new ArrayList<>();
        personalProducts = getFilteredShuffledProducts(personalProducts, excludedProductIds);
        updateExcludedIds(excludedProductIds, personalProducts);
        popularProducts = getFilteredShuffledProducts(popularProducts, excludedProductIds);
        updateExcludedIds(excludedProductIds, popularProducts);
        trendProducts = getFilteredShuffledProducts(trendProducts, excludedProductIds);

        return FindRecommendProductServiceResponse.builder()
            .personallyList(personalProducts)
            .popularList(popularProducts)
            .trendList(trendProducts)
            .build();
    }

    private List<ProductRecommend> getRecommendProduct(FindRecommendProductCommand command,
        RecommendType type) {
        LocalDate checkDate = LocalDate.parse(command.searchDate(),
            DateTimeFormatter.ofPattern("yyyyMMdd")).with(DayOfWeek.MONDAY);

        String redisKey = type.equals(PERSONAL) ?
            String.format(personalRecommendKey, checkDate, type.name(), command.accountId()) :
            String.format(productRecommendKey, checkDate, type.name());

        List<ProductRecommend> products = redisStoragePort.findDataList(redisKey,
            ProductRecommend.class);

        if (!products.isEmpty()) {
            return products;
        }

        products = type.equals(PERSONAL) ?
            getPersonalRecommendProduct(command.accountId()) :
            recommendStoragePort.findRecommendProductList(checkDate, type);

        if (!products.isEmpty()) {
            redisStoragePort.register(redisKey, toJsonString(products), type.equals(PERSONAL) ?
                personalRecommendTtl : productRecommendTtl);
        }

        return products;
    }

    private List<ProductRecommend> getPersonalRecommendProduct(Long accountId) {
        List<Long> productIds = orderClientPort.findProductIdByAccountId(accountId, 15);
        if (productIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<float[]> embeddings = new ArrayList<>();
        for (Long productId : productIds) {
            Product product = productStoragePort.findByIdAndDeleteYn(productId, "A");
            embeddings.add(embeddingUtil.embedToFloatArray(product.getEmbeddingDocument()));
        }

        float[] embedding = embeddingUtil.averageEmbeddings(embeddings);
        return elasticSearchClientPort.findByEmbedding(embedding).stream()
            .filter(product -> !productIds.contains(product.id()))
            .toList();
    }

    private List<ProductRecommend> getFilteredShuffledProducts(List<ProductRecommend> products,
        List<Long> excludedIds) {
        List<ProductRecommend> mutableProducts = new ArrayList<>(products);
        Collections.shuffle(mutableProducts);

        return mutableProducts.stream()
            .filter(product -> !excludedIds.contains(product.id()))
            .limit(10)
            .toList();
    }

    private void updateExcludedIds(List<Long> excludedIds, List<ProductRecommend> products) {
        excludedIds.addAll(products.stream()
            .map(ProductRecommend::id)
            .toList());
    }
}
