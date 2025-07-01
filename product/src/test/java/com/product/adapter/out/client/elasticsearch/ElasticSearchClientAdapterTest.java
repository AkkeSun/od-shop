package com.product.adapter.out.client.elasticsearch;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.adapter.out.client.elasticsearch.FindProductsEsResponse.Hit;
import com.product.adapter.out.client.elasticsearch.FindProductsEsResponse.Hit.DocumentSource;
import com.product.adapter.out.client.elasticsearch.FindProductsEsResponse.HitsWrapper;
import com.product.adapter.out.client.elasticsearch.FindProductsEsResponse.Total;
import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Category;
import com.product.domain.model.Product;
import com.product.domain.model.SortType;
import com.product.infrastructure.util.JsonUtil;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class ElasticSearchClientAdapterTest extends IntegrationTestSupport {

    @Autowired
    private ElasticSearchClientAdapter elasticSearchClientAdapter;

    private MockWebServer mockWebServer;

    @Autowired
    private EmbeddingUtil embeddingUtil;

    @BeforeEach
    void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(9200);
    }

    @AfterEach
    public void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    @Nested
    @DisplayName("[register] 엘라스틱서치에 상품을 등록하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품을 엘라스틱서치에 등록한다.")
        void success() {
            // given
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json"));
            Product product = Product.builder()
                .id(10L)
                .keywords(Set.of("keyword1", "keyword2"))
                .regDateTime(LocalDateTime.now())
                .category(Category.BOOKS)
                .build();
            float[] embedding = embeddingUtil.embedToFloatArray(
                "This is a sample product description for testing purposes."
            );

            // when // then
            elasticSearchClientAdapter.register(product, embedding);
        }

        @Test
        @DisplayName("[error] 상품 등록중 오류가 발생하는 경우 fallback 메소드를 호출한다.")
        void error(CapturedOutput output) {
            // given
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-type", "application/json"));
            Product product = Product.builder()
                .id(10L)
                .keywords(Set.of("keyword1", "keyword2"))
                .regDateTime(LocalDateTime.now())
                .category(Category.BOOKS)
                .build();
            float[] embedding = embeddingUtil.embedToFloatArray(
                "This is a sample product description for testing purposes."
            );

            // when
            RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> {
                elasticSearchClientAdapter.register(product, embedding);
            });

            // then
            assert output.toString().contains("[elasticSearch] registerFallback");
        }
    }

    @Nested
    @DisplayName("[deleteById] 엘라스틱서치 문서를 삭제하는 메소드")
    class Describe_deleteById {

        @Test
        @DisplayName("[success] 삭제 요청에 성공한다.")
        void success() {
            // given
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json"));
            Long productId = 20L;

            // when // then
            elasticSearchClientAdapter.deleteById(productId);
        }

        @Test
        @DisplayName("[error] 삭제 요청에 실패하는 경우 fallback 메소드를 호출한다.")
        void success2(CapturedOutput output) {
            // given
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-type", "application/json"));
            Long productId = 20L;

            // when
            elasticSearchClientAdapter.deleteById(productId);

            // then
            assert output.toString().contains("[elasticSearch] deleteByIdFallback (20) : ");
        }
    }

    @Nested
    @DisplayName("[findProducts] 엘라스틱서치 문서를 조회하는 메소드")
    class Describe_findProducts {

        @Test
        @DisplayName("[success] 조회중 오류 발생시 fallback 메소드를 호출한다.")
        void success(CapturedOutput output) {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .query("신발 추천")
                .category(Category.FASHION)
                .sortType(SortType.PRICE_ASC)
                .page(1)
                .size(10)
                .build();
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setHeader("Content-type", "application/json"));

            // when
            List<Product> products = elasticSearchClientAdapter.findProducts(command);

            // then
            assert products.isEmpty();
            assert output.toString().contains("[elasticSearch] findProductsFallback : ");
        }

        @Test
        @DisplayName("[success] 조회된 상품 정보가 없으면 빈 리스트를 응답한다.")
        void success2(CapturedOutput output) {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .query("신발 추천")
                .category(Category.FASHION)
                .sortType(SortType.PRICE_ASC)
                .page(1)
                .size(10)
                .build();
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json")
                .setBody(JsonUtil.toJsonString(FindProductsEsResponse.builder()
                    .hits(HitsWrapper.builder()
                        .total(Total.builder()
                            .value(0)
                            .build())
                        .build())
                    .build()))
            );

            // when
            List<Product> products = elasticSearchClientAdapter.findProducts(command);

            // then
            assert products.isEmpty();
            assert !output.toString().contains("[elasticSearch] findProductsFallback : ");
        }

        @Test
        @DisplayName("[success] 조회된 상품 정보가 있으면 상품 정보를 응답한다.")
        void success3() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .query("신발 추천")
                .category(Category.FASHION)
                .sortType(SortType.PRICE_ASC)
                .page(1)
                .size(10)
                .build();
            mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setHeader("Content-type", "application/json")
                .setBody(JsonUtil.toJsonString(FindProductsEsResponse.builder()
                    .hits(HitsWrapper.builder()
                        .total(Total.builder()
                            .value(1)
                            .build())
                        .hits(List.of(Hit.builder()
                            ._source(DocumentSource.builder()
                                .productId(10L)
                                .productName("테스트 상품")
                                .keywords("신발,운동화")
                                .sellerEmail("email")
                                .productImgUrl("http://example.com/image.jpg")
                                .price(10000L)
                                .salesCount(100L)
                                .reviewCount(50L)
                                .totalScore(4.5)
                                .category("FASHION")
                                .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0).format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                                .build())
                            .build()))
                        .build())
                    .build()))
            );

            // when
            List<Product> products = elasticSearchClientAdapter.findProducts(command);

            // then
            assert products.size() == 1;
            Product product = products.getFirst();
            assert product.getId() == 10L;
            assert product.getProductName().equals("테스트 상품");
            assert product.getKeywords().equals(Set.of("신발", "운동화"));
            assert product.getCategory() == Category.FASHION;
            assert product.getSellerEmail().equals("email");
            assert product.getProductImgUrl().equals("http://example.com/image.jpg");
            assert product.getPrice() == 10000L;
            assert product.getSalesCount() == 100L;
            assert product.getReviewCount() == 50L;
            assert product.getTotalScore() == 4.5;
        }
    }
}