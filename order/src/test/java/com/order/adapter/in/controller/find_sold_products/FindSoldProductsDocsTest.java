package com.order.adapter.in.controller.find_sold_products;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.common.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.order.RestDocsSupport;
import com.order.application.port.in.FindSoldProductsUseCase;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse;
import com.order.application.service.find_sold_products.FindSoldProductsServiceResponse.FindSoldProductsServiceResponseItem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class FindSoldProductsDocsTest extends RestDocsSupport {

    String apiName = "find_sold_products";
    FindSoldProductsUseCase useCase = mock(FindSoldProductsUseCase.class);

    @Override
    protected Object initController() {
        return new FindSoldProductsController(useCase);
    }

    @Nested
    @DisplayName("[findAll] 판매 상품 목록을 조회하는 API")
    class Describe_findAll {

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API를 요청했을 때 401 코드와 에러 메시지를 응답한다.")
        void error_unauthorized() throws Exception {
            // given
            String authorization = "testToken";
            given(useCase.findAll(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));

            // when, then
            performErrorDocument("", "", 0, 10, authorization, status().isUnauthorized(),
                "인증 토큰 미입력 혹은 만료된 토큰 입력");
        }

        @Test
        @DisplayName("[error] 상품 정보 조회 실패 시 500 코드와 에러 메시지를 응답한다.")
        void error_productNotFound() throws Exception {
            // given
            String authorization = "Bearer testToken";
            given(useCase.findAll(any())).willThrow(
                new CustomGrpcResponseError("NOT_FOUND - 상품 정보를 찾을 수 없습니다"));

            // when, then
            performDocument("productId", "1", 0, 10, authorization, status().isInternalServerError(),
                "상품 정보 조회 실패", "error",
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

        @Test
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 판매 상품 목록을 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer testToken";
            FindSoldProductsServiceResponse serviceResponse = FindSoldProductsServiceResponse.builder()
                .pageNumber(0)
                .pageSize(10)
                .totalElements(2)
                .totalPages(1)
                .orderList(List.of(
                    FindSoldProductsServiceResponseItem.builder()
                        .orderProductId(1L)
                        .customerId(100L)
                        .productName("나이키 에어맥스")
                        .productPrice(150000)
                        .buyQuantity(2)
                        .buyStatus("배송중")
                        .regDateTime("2025-01-15T10:30:00")
                        .build(),
                    FindSoldProductsServiceResponseItem.builder()
                        .orderProductId(2L)
                        .customerId(101L)
                        .productName("아디다스 울트라부스트")
                        .productPrice(200000)
                        .buyQuantity(1)
                        .buyStatus("배송완료")
                        .regDateTime("2025-01-16T14:20:00")
                        .build()
                ))
                .build();

            given(useCase.findAll(any())).willReturn(serviceResponse);

            // when then
            performDocument("productId", "1", 0, 10, authorization, status().isOk(),
                "success", apiName,
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.pageNumber").type(JsonFieldType.NUMBER)
                    .description("현재 페이지 번호"),
                fieldWithPath("data.pageSize").type(JsonFieldType.NUMBER)
                    .description("페이지 크기"),
                fieldWithPath("data.totalElements").type(JsonFieldType.NUMBER)
                    .description("전체 요소 수"),
                fieldWithPath("data.totalPages").type(JsonFieldType.NUMBER)
                    .description("전체 페이지 수"),
                fieldWithPath("data.orderList").type(JsonFieldType.ARRAY)
                    .description("판매 상품 목록"),
                fieldWithPath("data.orderList[].orderProductId").type(JsonFieldType.NUMBER)
                    .description("주문 상품 ID"),
                fieldWithPath("data.orderList[].customerId").type(JsonFieldType.NUMBER)
                    .description("구매자 ID"),
                fieldWithPath("data.orderList[].productName").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.orderList[].productPrice").type(JsonFieldType.NUMBER)
                    .description("상품 가격"),
                fieldWithPath("data.orderList[].buyQuantity").type(JsonFieldType.NUMBER)
                    .description("구매 수량"),
                fieldWithPath("data.orderList[].buyStatus").type(JsonFieldType.STRING)
                    .description("구매 상태"),
                fieldWithPath("data.orderList[].regDateTime").type(JsonFieldType.STRING)
                    .description("등록 일시")
            );
        }
    }

    private void performDocument(String searchType, String query, Integer page, Integer size,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(get("/orders/sold-products")
                .param("searchType", searchType)
                .param("query", query)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .header("Authorization", authorization))
            .andDo(print())
            .andExpect(status)
            .andDo(document(String.format("[%s] %s", apiName, docIdentifier),
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Order")
                        .summary("판매 상품 목록 조회 API")
                        .description("판매자가 판매한 상품 목록을 조회하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .queryParameters(
                            parameterWithName("searchType").description("검색 타입 (customerId, productId,  buyStatus)").optional(),
                            parameterWithName("query").description("검색어").optional(),
                            parameterWithName("page").description("페이지 번호 (기본값: 0)").optional(),
                            parameterWithName("size").description("페이지 크기 (기본값: 10)").optional()
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] " + apiName))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(String searchType, String query, Integer page, Integer size,
        String authorization,
        ResultMatcher status, String identifier) throws Exception {
        performDocument(searchType, query, page, size, authorization, status, identifier, "error",
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
