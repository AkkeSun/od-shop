package com.product.application.service.find_recommend_product;

import static com.product.infrastructure.util.JsonUtil.toJsonString;

import com.product.application.port.in.command.FindRecommendProductCommand;
import com.product.domain.model.Product;
import com.product.domain.model.ProductRecommend;
import com.product.fakeClass.DummyGeminiClientPort;
import com.product.fakeClass.DummyOrderClientPort;
import com.product.fakeClass.FakeElasticSearchClientPort;
import com.product.fakeClass.FakeProductStoragePort;
import com.product.fakeClass.FakeRecommendStoragePort;
import com.product.fakeClass.FakeRedisStoragePort;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class FindRecommendProductServiceTest {

    private final FindRecommendProductService service;
    private final DummyGeminiClientPort geminiClientPort;
    private final DummyOrderClientPort orderClientPort;
    private final FakeRedisStoragePort redisStoragePort;
    private final FakeProductStoragePort productStoragePort;
    private final FakeRecommendStoragePort recommendStoragePort;
    private final FakeElasticSearchClientPort elasticSearchClientPort;

    FindRecommendProductServiceTest() {
        this.geminiClientPort = new DummyGeminiClientPort();
        this.orderClientPort = new DummyOrderClientPort();
        this.redisStoragePort = new FakeRedisStoragePort();
        this.productStoragePort = new FakeProductStoragePort();
        this.recommendStoragePort = new FakeRecommendStoragePort();
        this.elasticSearchClientPort = new FakeElasticSearchClientPort();
        this.service = new FindRecommendProductService(geminiClientPort, orderClientPort,
            redisStoragePort, productStoragePort, recommendStoragePort, elasticSearchClientPort);

        ReflectionTestUtils
            .setField(service, "productRecommendKey", "product-recommend::%s-%s");
        ReflectionTestUtils
            .setField(service, "personalRecommendKey", "product-recommend::%s-%s-%s");
    }

    @AfterEach
    void reset() {
        redisStoragePort.database.clear();
        recommendStoragePort.database.clear();
    }

    @Nested
    @DisplayName("[findRecommendProductList] 추천 상품 리스트를 조회하는 메소드")
    class Describe_findRecommendProductList {

        @Test
        @DisplayName("[success] 조회된 상품이 없다면 빈 리스트를 응답한다.")
        void success() {
            // given
            FindRecommendProductCommand command = FindRecommendProductCommand.builder()
                .accountId(3L)
                .searchDate("20250501")
                .build();

            // when
            FindRecommendProductServiceResponse result = service.findRecommendProductList(command);

            // then
            assert result.personallyList().isEmpty();
            assert result.popularList().isEmpty();
            assert result.trendList().isEmpty();
        }

        @Test
        @DisplayName("[success] 레디스 캐시에 등록된 정보가 있다면 해당 정보를 응답한다.")
        void success2() {
            // given
            FindRecommendProductCommand command = FindRecommendProductCommand.builder()
                .accountId(3L)
                .searchDate("20250714")
                .build();
            ProductRecommend trendRecommend = ProductRecommend.builder()
                .productId(10L)
                .productName("trendProduct")
                .sellerEmail("seller1")
                .productImgUrl("img1")
                .build();
            ProductRecommend personalRecommend = ProductRecommend.builder()
                .productId(11L)
                .productName("personalProduct")
                .sellerEmail("seller2")
                .productImgUrl("img2")
                .build();
            ProductRecommend popularRecommend = ProductRecommend.builder()
                .productId(12L)
                .productName("popularProduct")
                .sellerEmail("seller3")
                .productImgUrl("img3")
                .build();
            redisStoragePort.register("product-recommend::2025-07-14-TREND",
                toJsonString(List.of(trendRecommend)), 100);
            redisStoragePort.register("product-recommend::2025-07-14-POPULAR",
                toJsonString(List.of(popularRecommend)), 100);
            redisStoragePort.register("product-recommend::2025-07-14-PERSONAL-3",
                toJsonString(List.of(personalRecommend)), 100);

            // when
            FindRecommendProductServiceResponse result = service.findRecommendProductList(command);

            // then

            assert result.personallyList().getFirst().productName()
                .equals(personalRecommend.productName());
            assert result.personallyList().getFirst().productImgUrl()
                .equals(personalRecommend.productImgUrl());
            assert result.personallyList().getFirst().productId()
                .equals(personalRecommend.productId());

            assert result.popularList().getFirst().productName()
                .equals(popularRecommend.productName());
            assert result.popularList().getFirst().productImgUrl()
                .equals(popularRecommend.productImgUrl());
            assert result.popularList().getFirst().productId()
                .equals(popularRecommend.productId());

            assert result.trendList().getFirst().productName()
                .equals(trendRecommend.productName());
            assert result.trendList().getFirst().productImgUrl()
                .equals(trendRecommend.productImgUrl());
            assert result.trendList().getFirst().productId()
                .equals(trendRecommend.productId());
        }

        @Test
        @DisplayName("[success] 레디스 캐시에 등록된 정보가 없다면 DB 에서 상품을 조회한 후 응답한다.")
        void success3() {
            // given
            FindRecommendProductCommand command = FindRecommendProductCommand.builder()
                .accountId(10L)
                .searchDate("20250714")
                .build();
            ProductRecommend trendRecommend = ProductRecommend.builder()
                .productId(10L)
                .productName("trendProduct")
                .sellerEmail("seller1")
                .productImgUrl("img1")
                .build();
            ProductRecommend personalRecommend = ProductRecommend.builder()
                .productId(11L)
                .productName("personalProduct")
                .sellerEmail("seller2")
                .productImgUrl("img2")
                .build();
            ProductRecommend popularRecommend = ProductRecommend.builder()
                .productId(12L)
                .productName("popularProduct")
                .sellerEmail("seller3")
                .productImgUrl("img3")
                .build();
            recommendStoragePort.database.add(trendRecommend);
            recommendStoragePort.database.add(popularRecommend);
            productStoragePort.database.add(Product.builder()
                .id(personalRecommend.productId())
                .price(50)
                .keywords(Set.of("keyword1"))
                .productName(popularRecommend.productName())
                .sellerEmail(personalRecommend.sellerEmail())
                .productImgUrl(popularRecommend.productImgUrl())
                .deleteYn("N")
                .build());
            elasticSearchClientPort.database.add(Product.builder()
                .id(15L)
                .productName(personalRecommend.productName())
                .keywords(Set.of("keyword1"))
                .productName(personalRecommend.productName())
                .sellerEmail(personalRecommend.sellerEmail())
                .productImgUrl(personalRecommend.productImgUrl())
                .deleteYn("N")
                .build());

            // when
            FindRecommendProductServiceResponse result = service.findRecommendProductList(command);

            // then
            assert result.personallyList().size() == 1;
            assert result.popularList().size() == 2;
        }

        @Test
        @DisplayName("[success] 중복 추천 상품을 제거한 후 응답한다.")
        void success4() {
            // given
            FindRecommendProductCommand command = FindRecommendProductCommand.builder()
                .accountId(3L)
                .searchDate("20250714")
                .build();
            ProductRecommend trendRecommend = ProductRecommend.builder()
                .productId(10L)
                .productName("trendProduct")
                .sellerEmail("seller1")
                .productImgUrl("img1")
                .build();
            ProductRecommend personalRecommend = ProductRecommend.builder()
                .productId(11L)
                .productName("personalProduct")
                .sellerEmail("seller2")
                .productImgUrl("img2")
                .build();
            ProductRecommend popularRecommend = ProductRecommend.builder()
                .productId(11L)
                .productName("popularProduct")
                .sellerEmail("seller3")
                .productImgUrl("img3")
                .build();
            redisStoragePort.register("product-recommend::2025-07-14-TREND",
                toJsonString(List.of(trendRecommend)), 100);
            redisStoragePort.register("product-recommend::2025-07-14-POPULAR",
                toJsonString(List.of(popularRecommend)), 100);
            redisStoragePort.register("product-recommend::2025-07-14-PERSONAL-3",
                toJsonString(List.of(personalRecommend)), 100);

            // when
            FindRecommendProductServiceResponse result = service.findRecommendProductList(command);

            // then
            assert result.personallyList().getFirst().productName()
                .equals(personalRecommend.productName());
            assert result.personallyList().getFirst().productImgUrl()
                .equals(personalRecommend.productImgUrl());
            assert result.personallyList().getFirst().productId()
                .equals(personalRecommend.productId());

            assert result.popularList().isEmpty();

            assert result.trendList().getFirst().productName()
                .equals(trendRecommend.productName());
            assert result.trendList().getFirst().productImgUrl()
                .equals(trendRecommend.productImgUrl());
            assert result.trendList().getFirst().productId()
                .equals(trendRecommend.productId());
        }
    }
}