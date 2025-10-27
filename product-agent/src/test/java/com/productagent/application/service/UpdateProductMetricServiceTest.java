package com.productagent.application.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.productagent.domain.model.Category;
import com.productagent.domain.model.Order;
import com.productagent.domain.model.Product;
import com.productagent.domain.model.ProductClickLog;
import com.productagent.domain.model.Review;
import com.productagent.fakeClass.FakeLogStoragePort;
import com.productagent.fakeClass.FakeOrderClientPort;
import com.productagent.fakeClass.FakeProductStoragePort;
import com.productagent.fakeClass.FakeReviewStoragePort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith(OutputCaptureExtension.class)
class UpdateProductMetricServiceTest {

    UpdateProductMetricService service;
    FakeLogStoragePort fakeLogStoragePort;
    FakeReviewStoragePort fakeReviewStoragePort;
    FakeOrderClientPort fakeOrderClientPort;
    FakeProductStoragePort fakeProductStoragePort;

    UpdateProductMetricServiceTest() {
        fakeLogStoragePort = new FakeLogStoragePort();
        fakeReviewStoragePort = new FakeReviewStoragePort();
        fakeOrderClientPort = new FakeOrderClientPort();
        fakeProductStoragePort = new FakeProductStoragePort();

        service = new UpdateProductMetricService(
            fakeLogStoragePort,
            fakeReviewStoragePort,
            fakeOrderClientPort,
            fakeProductStoragePort
        );
    }

    @BeforeEach
    void setup() {
        fakeLogStoragePort.database.clear();
        fakeLogStoragePort.lastMetricUpdateTime = LocalDateTime.of(2025, 1, 15, 0, 0, 0);
        fakeReviewStoragePort.database.clear();
        fakeOrderClientPort.orders.clear();
        fakeProductStoragePort.database.clear();

        // 테스트용 상품 추가
        Product product1 = Product.builder()
            .id(1L)
            .sellerId(10L)
            .productName("Test Product 1")
            .category(Category.DIGITAL)
            .price(10000)
            .quantity(100)
            .keywords(Set.of("test"))
            .deleteYn("N")
            .regDate(LocalDate.of(2025, 1, 1))
            .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .reviewCount(0)
            .reviewScore(0.0)
            .salesCount(0)
            .hitCount(0)
            .totalScore(0.0)
            .needsEsUpdate(false)
            .build();

        Product product2 = Product.builder()
            .id(2L)
            .sellerId(11L)
            .productName("Test Product 2")
            .category(Category.FASHION)
            .price(20000)
            .quantity(50)
            .keywords(Set.of("test"))
            .deleteYn("N")
            .regDate(LocalDate.of(2025, 1, 1))
            .regDateTime(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
            .reviewCount(0)
            .reviewScore(0.0)
            .salesCount(0)
            .hitCount(0)
            .totalScore(0.0)
            .needsEsUpdate(false)
            .build();

        fakeProductStoragePort.database.put(1L, product1);
        fakeProductStoragePort.database.put(2L, product2);
    }

    @Nested
    @DisplayName("[update] 상품 메트릭을 업데이트하는 메소드")
    class Describe_update {

        @Test
        @DisplayName("[success] 리뷰, 주문, 클릭 로그를 기반으로 상품 메트릭을 업데이트한다")
        void success(CapturedOutput output) {
            // given
            LocalDateTime startTime = LocalDateTime.of(2025, 1, 15, 0, 0, 1);
            LocalDateTime endTime = LocalDateTime.of(2025, 1, 15, 10, 0, 0);

            // 리뷰 데이터
            fakeReviewStoragePort.database.add(Review.builder()
                .id(1L)
                .productId(1L)
                .score(4.5)
                .regDateTime(LocalDateTime.of(2025, 1, 15, 1, 0, 0))
                .build());

            fakeReviewStoragePort.database.add(Review.builder()
                .id(2L)
                .productId(1L)
                .score(5.0)
                .regDateTime(LocalDateTime.of(2025, 1, 15, 2, 0, 0))
                .build());

            // 주문 데이터 - 각 Order는 한 번의 판매를 의미
            fakeOrderClientPort.orders.add(Order.builder()
                .productId(1L)
                .orderCount(2)
                .build());
            fakeOrderClientPort.orders.add(Order.builder()
                .productId(1L)
                .orderCount(1)
                .build());

            fakeOrderClientPort.orders.add(Order.builder()
                .productId(2L)
                .orderCount(1)
                .build());

            // 클릭 로그 데이터
            fakeLogStoragePort.database.put("click", java.util.List.of(
                ProductClickLog.builder()
                    .productId(1L)
                    .regDateTime("2025-01-15T03:00:00")
                    .build(),
                ProductClickLog.builder()
                    .productId(1L)
                    .regDateTime("2025-01-15T04:00:00")
                    .build(),
                ProductClickLog.builder()
                    .productId(2L)
                    .regDateTime("2025-01-15T05:00:00")
                    .build()
            ));

            // when
            service.update();

            // then
            Product updatedProduct1 = fakeProductStoragePort.database.get(1L);
            assertThat(updatedProduct1.getReviewCount()).isGreaterThan(0);
            assertThat(updatedProduct1.getReviewScore()).isGreaterThan(0);
            assertThat(updatedProduct1.getSalesCount()).isGreaterThan(0);
            assertThat(updatedProduct1.getHitCount()).isGreaterThan(0);
            assertThat(updatedProduct1.getTotalScore()).isGreaterThan(0);
            assertThat(updatedProduct1.isNeedsEsUpdate()).isTrue();

            Product updatedProduct2 = fakeProductStoragePort.database.get(2L);
            assertThat(updatedProduct2.getSalesCount()).isGreaterThan(0);
            assertThat(updatedProduct2.getHitCount()).isGreaterThan(0);
            assertThat(updatedProduct2.isNeedsEsUpdate()).isTrue();

            assertThat(output.toString()).contains("[updateMetric] success");
        }

        @Test
        @DisplayName("[success] 데이터가 없어도 정상 처리된다")
        void success_noData(CapturedOutput output) {
            // given - no data

            // when
            service.update();

            // then
            assertThat(output.toString()).contains("[updateMetric] success - 0");
        }

        @Test
        @DisplayName("[success] 삭제된 상품은 메트릭 업데이트에서 제외된다")
        void success_skipDeletedProduct() {
            // given
            Product deletedProduct = Product.builder()
                .id(3L)
                .sellerId(12L)
                .productName("Deleted Product")
                .category(Category.SPORTS)
                .deleteYn("Y") // 삭제된 상품
                .build();
            fakeProductStoragePort.database.put(3L, deletedProduct);

            fakeReviewStoragePort.database.add(Review.builder()
                .id(3L)
                .productId(3L)
                .score(4.0)
                .regDateTime(LocalDateTime.of(2025, 1, 15, 1, 0, 0))
                .build());

            // when
            service.update();

            // then
            Product product = fakeProductStoragePort.database.get(3L);
            assertThat(product.getReviewCount()).isEqualTo(0); // 업데이트 안됨
        }

        @Test
        @DisplayName("[success] 존재하지 않는 상품의 로그는 무시된다")
        void success_skipNonExistentProduct(CapturedOutput output) {
            // given
            fakeReviewStoragePort.database.add(Review.builder()
                .id(10L)
                .productId(999L)
                .score(4.5)
                .regDateTime(LocalDateTime.of(2025, 1, 15, 1, 0, 0))
                .build());

            // when
            service.update();

            // then
            assertThat(output.toString()).contains("[updateMetric] success - 0");
        }

        @Test
        @DisplayName("[success] 여러 로그가 한 상품에 적용된다")
        void success_multipleLogsForOneProduct(CapturedOutput output) {
            // given
            for (int i = 0; i < 5; i++) {
                fakeReviewStoragePort.database.add(Review.builder()
                    .id((long) i)
                    .productId(1L)
                    .score(4.0 + (i * 0.1))
                    .regDateTime(LocalDateTime.of(2025, 1, 15, i, 0, 0))
                    .build());
            }

            for (int i = 0; i < 3; i++) {
                fakeOrderClientPort.orders.add(Order.builder()
                    .productId(1L)
                    .orderCount(1)
                    .build());
            }

            // when
            service.update();

            // then
            Product updatedProduct = fakeProductStoragePort.database.get(1L);
            assertThat(updatedProduct.getReviewCount()).isGreaterThan(0);
            assertThat(updatedProduct.getSalesCount()).isGreaterThan(0);
            assertThat(output.toString()).contains("[updateMetric] success");
        }
    }
}
