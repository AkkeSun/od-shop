package com.productagent.domain.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Nested
    @DisplayName("[description] 카테고리 설명을 반환하는 메소드")
    class Describe_description {

        @Test
        @DisplayName("[success] DIGITAL 카테고리의 설명을 반환한다")
        void success_digital() {
            // given
            Category category = Category.DIGITAL;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("전자제품");
        }

        @Test
        @DisplayName("[success] FASHION 카테고리의 설명을 반환한다")
        void success_fashion() {
            // given
            Category category = Category.FASHION;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("패션");
        }

        @Test
        @DisplayName("[success] SPORTS 카테고리의 설명을 반환한다")
        void success_sports() {
            // given
            Category category = Category.SPORTS;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("스포츠");
        }

        @Test
        @DisplayName("[success] FOOD 카테고리의 설명을 반환한다")
        void success_food() {
            // given
            Category category = Category.FOOD;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("식품");
        }

        @Test
        @DisplayName("[success] LIFE 카테고리의 설명을 반환한다")
        void success_life() {
            // given
            Category category = Category.LIFE;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("생활용품");
        }

        @Test
        @DisplayName("[success] TOTAL 카테고리의 설명을 반환한다")
        void success_total() {
            // given
            Category category = Category.TOTAL;

            // when
            String description = category.description();

            // then
            assertThat(description).isEqualTo("전체");
        }
    }

    @Nested
    @DisplayName("[Enum] 카테고리 Enum의 기본 동작")
    class Describe_enum {

        @Test
        @DisplayName("[success] 모든 카테고리 값을 조회할 수 있다")
        void success_values() {
            // when
            Category[] categories = Category.values();

            // then
            assertThat(categories).hasSize(6);
            assertThat(categories).containsExactly(
                Category.DIGITAL,
                Category.FASHION,
                Category.SPORTS,
                Category.FOOD,
                Category.LIFE,
                Category.TOTAL
            );
        }

        @Test
        @DisplayName("[success] 문자열로 카테고리를 조회할 수 있다")
        void success_valueOf() {
            // when
            Category digital = Category.valueOf("DIGITAL");
            Category fashion = Category.valueOf("FASHION");

            // then
            assertThat(digital).isEqualTo(Category.DIGITAL);
            assertThat(fashion).isEqualTo(Category.FASHION);
        }

        @Test
        @DisplayName("[success] 같은 카테고리는 동등하다")
        void success_equality() {
            // given
            Category category1 = Category.DIGITAL;
            Category category2 = Category.DIGITAL;

            // then
            assertThat(category1).isEqualTo(category2);
            assertThat(category1 == category2).isTrue();
        }

        @Test
        @DisplayName("[success] 다른 카테고리는 동등하지 않다")
        void success_inequality() {
            // given
            Category category1 = Category.DIGITAL;
            Category category2 = Category.FASHION;

            // then
            assertThat(category1).isNotEqualTo(category2);
            assertThat(category1 == category2).isFalse();
        }
    }
}
