package com.productagent.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductTest {

    @Nested
    @DisplayName("[updateHitCount] 조회수를 증가시키는 메소드")
    class Describe_updateHitCount {

        @Test
        @DisplayName("[success] 조회수가 1 증가한다")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .hitCount(10)
                .build();

            // when
            product.updateHitCount();

            // then
            assertThat(product.getHitCount()).isEqualTo(11);
        }

        @Test
        @DisplayName("[success] 초기값 0에서 조회수가 1 증가한다")
        void success_fromZero() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .hitCount(0)
                .build();

            // when
            product.updateHitCount();

            // then
            assertThat(product.getHitCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("[updateReviewInfo] 리뷰 정보를 업데이트하는 메소드")
    class Describe_updateReviewInfo {

        @Test
        @DisplayName("[success] 첫 리뷰 등록 시 리뷰 수와 평점이 업데이트된다")
        void success_firstReview() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .reviewCount(0)
                .reviewScore(0.0)
                .build();

            Review review = Review.builder()
                .id(1L)
                .productId(1L)
                .score(4.5)
                .review("Good product")
                .build();

            // when
            product.updateReviewInfo(review);

            // then
            assertThat(product.getReviewCount()).isEqualTo(1);
            assertThat(product.getReviewScore()).isEqualTo(4.5);
        }

        @Test
        @DisplayName("[success] 두 번째 리뷰 등록 시 평균 평점이 계산된다")
        void success_secondReview() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .reviewCount(1)
                .reviewScore(4.0)
                .build();

            Review review = Review.builder()
                .id(2L)
                .productId(1L)
                .score(5.0)
                .review("Excellent product")
                .build();

            // when
            product.updateReviewInfo(review);

            // then
            assertThat(product.getReviewCount()).isEqualTo(2);
            assertThat(product.getReviewScore()).isEqualTo(4.5); // (4.0 + 5.0) / 2
        }

        @Test
        @DisplayName("[success] 여러 리뷰 등록 시 평균 평점이 정확히 계산된다")
        void success_multipleReviews() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .reviewCount(2)
                .reviewScore(4.5)
                .build();

            Review review = Review.builder()
                .id(3L)
                .productId(1L)
                .score(3.0)
                .review("Average product")
                .build();

            // when
            product.updateReviewInfo(review);

            // then
            assertThat(product.getReviewCount()).isEqualTo(3);
            assertThat(product.getReviewScore()).isEqualTo(2.5); // (4.5 + 3.0) / 3
        }
    }

    @Nested
    @DisplayName("[updateSalesCount] 판매 수를 증가시키는 메소드")
    class Describe_updateSalesCount {

        @Test
        @DisplayName("[success] 판매 수가 1 증가한다")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(5)
                .build();

            // when
            product.updateSalesCount();

            // then
            assertThat(product.getSalesCount()).isEqualTo(6);
        }

        @Test
        @DisplayName("[success] 초기값 0에서 판매 수가 1 증가한다")
        void success_fromZero() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(0)
                .build();

            // when
            product.updateSalesCount();

            // then
            assertThat(product.getSalesCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("[updateTotalScore] 총점을 계산하는 메소드")
    class Describe_updateTotalScore {

        @Test
        @DisplayName("[success] 모든 지표가 0일 때 총점이 0이다")
        void success_allZero() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(0)
                .reviewCount(0)
                .hitCount(0)
                .reviewScore(0.0)
                .needsEsUpdate(false)
                .build();

            // when
            product.updateTotalScore();

            // then
            assertThat(product.getTotalScore()).isEqualTo(0.0);
            assertThat(product.isNeedsEsUpdate()).isTrue();
            assertThat(product.getUpdateDateTime()).isNotNull();
        }

        @Test
        @DisplayName("[success] 판매 수만 있을 때 총점이 계산된다")
        void success_onlySales() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(1000) // 10% of max (10000)
                .reviewCount(0)
                .hitCount(0)
                .reviewScore(0.0)
                .build();

            // when
            product.updateTotalScore();

            // then
            // salesScore = 1000/10000 = 0.1
            // totalScore = 0.1 * 0.4 * 100 = 4.0
            assertThat(product.getTotalScore()).isCloseTo(4.0, org.assertj.core.api.Assertions.within(0.01));
            assertThat(product.isNeedsEsUpdate()).isTrue();
        }

        @Test
        @DisplayName("[success] 리뷰 평점만 있을 때 총점이 계산된다")
        void success_onlyReview() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(0)
                .reviewCount(0)
                .hitCount(0)
                .reviewScore(5.0) // max score
                .build();

            // when
            product.updateTotalScore();

            // then
            // reviewScoreNormalized = 5.0/5.0 = 1.0
            // totalScore = 1.0 * 0.3 * 100 = 30.0
            assertThat(product.getTotalScore()).isEqualTo(30.0);
            assertThat(product.isNeedsEsUpdate()).isTrue();
        }

        @Test
        @DisplayName("[success] 모든 지표가 최대값일 때 총점이 100이다")
        void success_allMax() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(10000) // max
                .reviewCount(500) // max
                .hitCount(10000) // max
                .reviewScore(5.0) // max
                .build();

            // when
            product.updateTotalScore();

            // then
            // salesScore = 1.0, reviewCntScore = 1.0, hitScore = 1.0, reviewScoreNormalized = 1.0
            // totalScore = (1.0*0.4 + 1.0*0.2 + 1.0*0.1 + 1.0*0.3) * 100 = 100.0
            assertThat(product.getTotalScore()).isEqualTo(100.0);
            assertThat(product.isNeedsEsUpdate()).isTrue();
        }

        @Test
        @DisplayName("[success] 지표가 최대값을 초과해도 1.0으로 제한된다")
        void success_exceedMax() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(20000) // 2x max
                .reviewCount(1000) // 2x max
                .hitCount(20000) // 2x max
                .reviewScore(5.0) // max
                .build();

            // when
            product.updateTotalScore();

            // then
            // All scores capped at 1.0
            assertThat(product.getTotalScore()).isEqualTo(100.0);
        }

        @Test
        @DisplayName("[success] 실제 시나리오: 중간 정도의 인기 상품")
        void success_normalProduct() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .salesCount(500) // 5% of max
                .reviewCount(50) // 10% of max
                .hitCount(1000) // 10% of max
                .reviewScore(4.2) // 84% of max
                .build();

            // when
            product.updateTotalScore();

            // then
            // salesScore = 0.05, reviewCntScore = 0.1, hitScore = 0.1, reviewScoreNormalized = 0.84
            // totalScore = (0.05*0.4 + 0.1*0.2 + 0.1*0.1 + 0.84*0.3) * 100 = 30.2
            assertThat(product.getTotalScore()).isCloseTo(30.2, org.assertj.core.api.Assertions.within(0.01));
        }
    }

    @Nested
    @DisplayName("[getEmbeddingDocument] 임베딩을 위한 문서를 생성하는 메소드")
    class Describe_getEmbeddingDocument {

        @Test
        @DisplayName("[success] 상품 정보를 포함한 임베딩 문서를 생성한다")
        void success() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("갤럭시 S24")
                .category(Category.DIGITAL)
                .price(1200000)
                .keywords(Set.of("스마트폰", "삼성", "안드로이드"))
                .build();

            // when
            String embeddingDocument = product.getEmbeddingDocument();

            // then
            assertThat(embeddingDocument).contains("갤럭시 S24");
            assertThat(embeddingDocument).contains("전자제품");
            assertThat(embeddingDocument).contains("1200000원");
            assertThat(embeddingDocument).contains("스마트폰");
            assertThat(embeddingDocument).contains("삼성");
            assertThat(embeddingDocument).contains("안드로이드");
        }

        @Test
        @DisplayName("[success] 모든 카테고리에 대한 임베딩 문서가 올바르게 생성된다")
        void success_allCategories() {
            // given
            Product digitalProduct = Product.builder()
                .productName("노트북")
                .category(Category.DIGITAL)
                .price(1500000)
                .keywords(Set.of("컴퓨터"))
                .build();

            Product fashionProduct = Product.builder()
                .productName("티셔츠")
                .category(Category.FASHION)
                .price(30000)
                .keywords(Set.of("의류"))
                .build();

            Product sportsProduct = Product.builder()
                .productName("축구공")
                .category(Category.SPORTS)
                .price(50000)
                .keywords(Set.of("운동"))
                .build();

            // when & then
            assertThat(digitalProduct.getEmbeddingDocument()).contains("전자제품");
            assertThat(fashionProduct.getEmbeddingDocument()).contains("패션");
            assertThat(sportsProduct.getEmbeddingDocument()).contains("스포츠");
        }

        @Test
        @DisplayName("[success] 키워드가 여러 개일 때 쉼표로 구분된다")
        void success_multipleKeywords() {
            // given
            Product product = Product.builder()
                .productName("무선 이어폰")
                .category(Category.DIGITAL)
                .price(200000)
                .keywords(Set.of("블루투스", "음악", "무선"))
                .build();

            // when
            String embeddingDocument = product.getEmbeddingDocument();

            // then
            assertThat(embeddingDocument).contains("블루투스");
            assertThat(embeddingDocument).contains("음악");
            assertThat(embeddingDocument).contains("무선");
        }
    }

    @Nested
    @DisplayName("[updateNeedsEsUpdate] ES 업데이트 필요 여부를 설정하는 메소드")
    class Describe_updateNeedsEsUpdate {

        @Test
        @DisplayName("[success] needsEsUpdate를 true로 설정한다")
        void success_setTrue() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .needsEsUpdate(false)
                .build();

            // when
            product.updateNeedsEsUpdate(true);

            // then
            assertThat(product.isNeedsEsUpdate()).isTrue();
        }

        @Test
        @DisplayName("[success] needsEsUpdate를 false로 설정한다")
        void success_setFalse() {
            // given
            Product product = Product.builder()
                .id(1L)
                .productName("Test Product")
                .needsEsUpdate(true)
                .build();

            // when
            product.updateNeedsEsUpdate(false);

            // then
            assertThat(product.isNeedsEsUpdate()).isFalse();
        }
    }
}
