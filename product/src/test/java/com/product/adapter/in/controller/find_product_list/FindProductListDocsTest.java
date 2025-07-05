package com.product.adapter.in.controller.find_product_list;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.product.RestDocsSupport;
import com.product.application.port.in.FindProductListUseCase;
import com.product.application.service.find_product_list.FindProductListServiceResponse;
import com.product.application.service.find_product_list.FindProductListServiceResponse.FindProductListServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class FindProductListDocsTest extends RestDocsSupport {

    private final FindProductListUseCase findProductListUseCase = mock(
        FindProductListUseCase.class);

    @Override
    protected Object initController() {
        return new FindProductListController(findProductListUseCase);
    }

    @Nested
    @DisplayName("[findProductList] 상품 목록을 조회하는 API")
    class Describe_findProductList {

        @Test
        @DisplayName("[success] 상품 목록을 성공적으로 조회하면 상품 정보 리스트를 응답한다.")
        void success() throws Exception {
            // given
            FindProductListServiceResponse response = FindProductListServiceResponse.builder()
                .page(0)
                .size(10)
                .productCount(2)
                .productList(List.of(
                    FindProductListServiceResponseItem.builder()
                        .id(10L)
                        .productName("productName1")
                        .sellerEmail("sellerEmail1")
                        .productImgUrl("img1")
                        .price(50000)
                        .build(),
                    FindProductListServiceResponseItem.builder()
                        .id(12L)
                        .productName("productName2")
                        .sellerEmail("sellerEmail2")
                        .productImgUrl("img2")
                        .price(50000)
                        .build())
                )
                .build();
            FindProductListRequest request = FindProductListRequest.builder()
                .query("검색어")
                .category("FASHION")
                .sortType("PRICE_ASC")
                .page(0)
                .size(10)
                .build();

            when(findProductListUseCase.findProductList(any()))
                .thenReturn(response);

            // when then
            performDocument(status().isOk(), request, "success",
                "[response] find-product-list",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.page").type(JsonFieldType.NUMBER)
                    .description("조회 페이지"),
                fieldWithPath("data.size").type(JsonFieldType.NUMBER)
                    .description("조회 사이즈"),
                fieldWithPath("data.productCount").type(JsonFieldType.NUMBER)
                    .description("조회된 상품 수"),
                fieldWithPath("data.productList").type(JsonFieldType.ARRAY)
                    .description("상품 리스트"),
                fieldWithPath("data.productList[].id").type(JsonFieldType.NUMBER)
                    .description("상품 아이디"),
                fieldWithPath("data.productList[].productName").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.productList[].sellerEmail").type(JsonFieldType.STRING)
                    .description("판매자 이메일"),
                fieldWithPath("data.productList[].productImgUrl").type(JsonFieldType.STRING)
                    .description("상품 이미지 주소"),
                fieldWithPath("data.productList[].price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            );
        }
    }

    @Test
    @DisplayName("[error] 검색어를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
    void error1() throws Exception {
        // given
        FindProductListRequest request = FindProductListRequest.builder()
            .category("FASHION")
            .sortType("PRICE_ASC")
            .page(0)
            .size(10)
            .build();

        // when then
        performErrorDocument(status().isBadRequest(), request, "검색어 미입력");
    }


    @Test
    @DisplayName("[error] 카테고리를 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
    void error2() throws Exception {
        // given
        FindProductListRequest request = FindProductListRequest.builder()
            .query("검색어")
            .sortType("PRICE_ASC")
            .page(0)
            .size(10)
            .build();

        // when then
        performErrorDocument(status().isBadRequest(), request, "카테고리 미입력");
    }

    @Test
    @DisplayName("[error] 유효하지 않은 카테고리를 입력했을 때 400코드와 에러 메시지를 응답한다.")
    void error3() throws Exception {
        // given
        FindProductListRequest request = FindProductListRequest.builder()
            .query("검색어")
            .category("error")
            .sortType("PRICE_ASC")
            .page(0)
            .size(10)
            .build();

        // when then
        performErrorDocument(status().isBadRequest(), request, "유효하지 않은 카테고리 입력");
    }

    @Test
    @DisplayName("[error] 정렬타입을 입력하지 않았을 때 400코드와 에러 메시지를 응답한다.")
    void error4() throws Exception {
        // given
        FindProductListRequest request = FindProductListRequest.builder()
            .query("검색어")
            .category("FASHION")
            .sortType("error")
            .page(0)
            .size(10)
            .build();

        // when then
        performErrorDocument(status().isBadRequest(), request, "유효하지 않은 정렬타입 입력");
    }

    @Test
    @DisplayName("[error] 유효하지 않은 정렬타입을 입력했을 때 400코드와 에러 메시지를 응답한다.")
    void error5() throws Exception {
        // given
        FindProductListRequest request = FindProductListRequest.builder()
            .query("검색어")
            .category("FASHION")
            .sortType("")
            .page(0)
            .size(10)
            .build();

        // when then
        performErrorDocument(status().isBadRequest(), request, "정렬타입 미입력");
    }

    private void performDocument(ResultMatcher status, FindProductListRequest request,
        String docIdentifier, String responseSchema, FieldDescriptor... responseFields)
        throws Exception {

        mockMvc.perform(get("/products")
                .param("query", request.getQuery())
                .param("category", request.getCategory())
                .param("sortType", request.getSortType())
                .param("page", String.valueOf(request.getPage()))
                .param("size", String.valueOf(request.getSize()))
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document("[find-product-list] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Product")
                        .summary("상품 목록 조회 API")
                        .description("상품 목록을 조회하는 API 입니다.")
                        .queryParameters(
                            parameterWithName("query").description("검색어"),
                            parameterWithName("category").description(
                                "카테고리 (ELECTRONICS, FASHION, HOME_APPLIANCES, BEAUTY, BOOKS, SPORTS FOOD, TOYS, FURNITURE, AUTOMOTIVE, HEALTH, TOTAL"),
                            parameterWithName("sortType").description(
                                "정렬 타입 (PRICE_ASC, PRICE_DESC, SALES_COUNT_ASC, SALES_COUNT_DESC, REVIEW_CNT_ASC, REVIEW_CNT_DESC, REG_DATE_TIME_ASC, REG_DATE_TIME_DESC, TOTAL_SCORE_ASC, TOTAL_SCORE_DESC)"),
                            parameterWithName("page").description("조회 페이지 (기본값 0)").optional(),
                            parameterWithName("size").description("조회 사이즈 (기본값 10)").optional()
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] find-product-list"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(ResultMatcher status, FindProductListRequest request,
        String docIdentifier) throws Exception {

        performDocument(status, request, docIdentifier, "[response] error",
            fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                .description("상태 코드"),
            fieldWithPath("message").type(JsonFieldType.STRING)
                .description("상태 메시지"),
            fieldWithPath("data").type(JsonFieldType.OBJECT)
                .description("응답 데이터"),
            fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                .description("에러 코드"),
            fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                .description("에러 메시지")
        );
    }
}
