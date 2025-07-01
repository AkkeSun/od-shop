package com.product.adapter.in.controller.find_product_list;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Category;
import com.product.domain.model.SortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListRequestTest {

    @Nested
    @DisplayName("[toCommand] API 요청 객체를 command 로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] API 요청 객체를 command 로 잘 변환하는지 확인한다.")
        void success() {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("FASHION")
                .sortType("SALES_COUNT_ASC")
                .page(1)
                .size(10)
                .build();

            // when
            FindProductListCommand result = request.toCommand();

            // then
            assert result.query().equals(request.getQuery());
            assert result.category().equals(Category.FASHION);
            assert result.sortType().equals(SortType.SALES_COUNT_ASC);
            assert result.page() == 1;
            assert result.size() == 10;
        }

        @Test
        @DisplayName("[success] 사이즈를 입력하지 않았을 때 10으로 변환하는지 확인한다.")
        void success2() {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .query("test")
                .category("FASHION")
                .sortType("SALES_COUNT_ASC")
                .page(1)
                .build();

            // when
            FindProductListCommand result = request.toCommand();

            // then
            assert result.query().equals(request.getQuery());
            assert result.category().equals(Category.FASHION);
            assert result.sortType().equals(SortType.SALES_COUNT_ASC);
            assert result.page() == 1;
            assert result.size() == 10;
        }
    }
}