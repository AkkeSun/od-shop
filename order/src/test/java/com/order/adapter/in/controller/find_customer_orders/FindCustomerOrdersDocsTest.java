package com.order.adapter.in.controller.find_customer_orders;

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

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.order.RestDocsSupport;
import com.order.application.port.in.FindCustomerOrdersUseCase;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse;
import com.order.application.service.find_customer_orders.FindCustomerOrdersServiceResponse.FindCustomerOrdersServiceResponseItem;
import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.common.infrastructure.exception.ErrorCode;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class FindCustomerOrdersDocsTest extends RestDocsSupport {

    String apiName = "find_customer_orders";
    FindCustomerOrdersUseCase useCase = mock(FindCustomerOrdersUseCase.class);

    @Override
    protected Object initController() {
        return new FindCustomerOrdersController(useCase);
    }

    @Nested
    @DisplayName("[findAll] 고객 주문 목록을 조회하는 API")
    class Describe_findAll {

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API를 요청했을 때 401 코드와 에러 메시지를 응답한다.")
        void error_unauthorized() throws Exception {
            // given
            String authorization = "testToken";
            given(useCase.findAll(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));

            // when, then
            performErrorDocument(0, 10, authorization, status().isUnauthorized(),
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
            performDocument(0, 10, authorization, status().isInternalServerError(),
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
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 주문 목록을 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer testToken";
            FindCustomerOrdersServiceResponse serviceResponse = FindCustomerOrdersServiceResponse.builder()
                .pageNumber(0)
                .pageSize(10)
                .totalElements(2)
                .totalPages(1)
                .orderList(List.of(
                    FindCustomerOrdersServiceResponseItem.builder()
                        .orderNumber(12345L)
                        .orderDateTime("2025-01-15T10:30:00")
                        .primaryProductName("나이키 에어맥스")
                        .primaryProductImg("https://example.com/image1.jpg")
                        .primaryProductBuyStatus("배송중")
                        .totalProductCnt(2)
                        .totalPrice(150000)
                        .build(),
                    FindCustomerOrdersServiceResponseItem.builder()
                        .orderNumber(12346L)
                        .orderDateTime("2025-01-16T14:20:00")
                        .primaryProductName("아디다스 울트라부스트")
                        .primaryProductImg("https://example.com/image2.jpg")
                        .primaryProductBuyStatus("배송완료")
                        .totalProductCnt(1)
                        .totalPrice(200000)
                        .build()
                ))
                .build();

            given(useCase.findAll(any())).willReturn(serviceResponse);

            // when then
            performDocument(0, 10, authorization, status().isOk(),
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
                    .description("주문 목록"),
                fieldWithPath("data.orderList[].orderNumber").type(JsonFieldType.NUMBER)
                    .description("주문 번호"),
                fieldWithPath("data.orderList[].orderDateTime").type(JsonFieldType.STRING)
                    .description("주문 일시"),
                fieldWithPath("data.orderList[].primaryProductName").type(JsonFieldType.STRING)
                    .description("대표 상품명"),
                fieldWithPath("data.orderList[].primaryProductImg").type(JsonFieldType.STRING)
                    .description("대표 상품 이미지 URL"),
                fieldWithPath("data.orderList[].primaryProductBuyStatus").type(JsonFieldType.STRING)
                    .description("대표 상품 구매 상태"),
                fieldWithPath("data.orderList[].totalProductCnt").type(JsonFieldType.NUMBER)
                    .description("전체 상품 개수"),
                fieldWithPath("data.orderList[].totalPrice").type(JsonFieldType.NUMBER)
                    .description("총 금액")
            );
        }
    }

    private void performDocument(Integer page, Integer size,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(get("/orders/buy-products")
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
                        .summary("고객 주문 목록 조회 API")
                        .description("로그인한 고객의 주문 목록을 조회하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .queryParameters(
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

    private void performErrorDocument(Integer page, Integer size,
        String authorization,
        ResultMatcher status, String identifier) throws Exception {
        performDocument(page, size, authorization, status, identifier, "error",
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
