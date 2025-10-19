package com.product.application.service.find_product_list;

import com.product.application.port.in.command.FindProductListCommand;
import com.product.domain.model.Product;
import com.product.domain.model.SortType;
import com.product.fakeClass.DummyMessageProducerPort;
import com.product.fakeClass.FakeElasticSearchClientPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListServiceTest {

    private final FindProductListService service;
    private final DummyMessageProducerPort messageProducerPort;
    private final FakeElasticSearchClientPort elasticSearchClientPort;

    FindProductListServiceTest() {
        this.messageProducerPort = new DummyMessageProducerPort();
        this.elasticSearchClientPort = new FakeElasticSearchClientPort();
        this.service = new FindProductListService(messageProducerPort, elasticSearchClientPort);
    }

    @AfterEach
    void reset() {
        elasticSearchClientPort.database.clear();
    }

    @Nested
    @DisplayName("[findProductList] 상품 리스트를 조회하는 메소드")
    class Describe_findProductList {

        @Test
        @DisplayName("[sucess] 조회된 정보가 없다면 빈 리스트를 응답한다.")
        void success() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .query("멋진 상품")
                .category(Category.FASHION)
                .sortType(SortType.PRICE_ASC)
                .page(0)
                .size(10)
                .build();

            // when
            FindProductListServiceResponse result = service.findProductList(command);

            // then
            assert result.page() == 0;
            assert result.size() == 10;
            assert result.productCount() == 0;
            assert result.productList().isEmpty();
        }

        @Test
        @DisplayName("[sucess] 조회된 정보가 있다면 해당 정보를 응답한다.")
        void success2() {
            // given
            FindProductListCommand command = FindProductListCommand.builder()
                .query("멋진 상품")
                .category(Category.FASHION)
                .sortType(SortType.PRICE_ASC)
                .page(0)
                .size(10)
                .build();
            Product product1 = Product.builder()
                .id(10L)
                .productName("멋진 상품 입니다")
                .category(Category.FASHION)
                .deleteYn("N")
                .build();
            Product product2 = Product.builder()
                .id(11L)
                .productName("정말 멋진 상품")
                .category(Category.FASHION)
                .deleteYn("N")
                .build();
            elasticSearchClientPort.database.add(product1);
            elasticSearchClientPort.database.add(product2);

            // when
            FindProductListServiceResponse result = service.findProductList(command);

            // then
            assert result.page() == 0;
            assert result.size() == 10;
            assert result.productCount() == 2;
            assert result.productList().getFirst().productName().equals(product1.getProductName());
            assert result.productList().getLast().productName().equals(product2.getProductName());
        }
    }
}