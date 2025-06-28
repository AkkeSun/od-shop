package com.product.adapter.out.client.elasticsearch;

import static com.product.domain.model.Category.FASHION;
import static com.product.domain.model.Category.TOTAL;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductsEsRequestTest {

    @Nested
    @DisplayName("[of] FindProductListCommand 를 FindProductsEsRequest 로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 카테고리가 TOTAL 일 때 빈 must 조건을 가진 FindProductsEsRequest 를 반환한다")
        void success1() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .category(TOTAL)
                .query("test")
                .sortType(SortType.PRICE_ASC)
                .page(0)
                .size(10)
                .build();

            // when
            FindProductsEsRequest result = FindProductsEsRequest.of(command);

            // then
            assert result.query().bool().must().isEmpty();
            assert result.query().bool().should().size() == 2;
            assert result.query().bool().should().get(0).get("match").get("keywords")
                .equals("test");
            assert result.query().bool().should().get(1).get("match_phrase").get("productName")
                .equals("test");
            assert result.query().bool().minimumShouldMatch() == 1;
            assert result.sort().size() == 1;
            assert result.sort().getFirst().get("price").equals("asc");
            assert result.from() == 0;
            assert result.size() == 10;

        }

        @Test
        @DisplayName("[success] 정렬 타입이 decending 일 때 역순 정렬을 가진 FindProductsEsRequest 를 반환한다")
        void success2() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .category(FASHION)
                .query("test")
                .sortType(SortType.PRICE_DESC)
                .page(0)
                .size(10)
                .build();

            // when
            FindProductsEsRequest result = FindProductsEsRequest.of(command);

            // then
            assert result.query().bool().must().size() == 1;
            assert result.query().bool().must().getFirst().get("term").get("category")
                .equals("FASHION");
            assert result.query().bool().should().size() == 2;
            assert result.query().bool().should().get(0).get("match").get("keywords")
                .equals("test");
            assert result.query().bool().should().get(1).get("match_phrase").get("productName")
                .equals("test");
            assert result.query().bool().minimumShouldMatch() == 1;
            assert result.sort().size() == 1;
            assert result.sort().getFirst().get("price").equals("desc");
            assert result.from() == 0;
            assert result.size() == 10;
        }
    }
}