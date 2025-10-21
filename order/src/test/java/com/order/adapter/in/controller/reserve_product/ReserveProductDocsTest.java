package com.order.adapter.in.controller.reserve_product;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.CustomGrpcResponseError;
import com.common.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.order.RestDocsSupport;
import com.order.application.port.in.ReserveProductUseCase;
import com.order.application.service.reserve_product.ReserveProductServiceResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class ReserveProductDocsTest extends RestDocsSupport {

    String apiName = "reserve_product";
    ReserveProductUseCase useCase = mock(ReserveProductUseCase.class);

    @Override
    protected Object initController() {
        return new ReserveProductController(useCase);
    }

    @Nested
    @DisplayName("[reserveProducts] 상품을 예약하는 API")
    class Describe_reserveProducts {

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API를 요청했을 때 401 코드와 에러 메시지를 응답한다.")
        void error_unauthorized() throws Exception {
            // given
            List<ReserveProductRequest> request = createValidRequest();
            String authorization = "testToken";
            given(useCase.reservation(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));

            // when, then
            performErrorDocument(request, authorization, status().isUnauthorized(),
                "인증 토큰 미입력 혹은 만료된 토큰 입력");
        }

        // Note: List 요소의 개별 필드 validation은 Spring의 제약으로 인해 작동하지 않습니다.
        // List 전체에 대한 validation만 가능합니다.

        @Test
        @DisplayName("[error] 상품이 존재하지 않을 때 500 코드와 에러 메시지를 응답한다.")
        void error_productNotFound() throws Exception {
            // given
            List<ReserveProductRequest> request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.reservation(any())).willThrow(
                new CustomGrpcResponseError("NOT_FOUND - 상품 정보를 찾을 수 없습니다 - 1"));

            // when, then
            performDocument(request, authorization, status().isInternalServerError(),
                "상품 정보 없음", "error",
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
        @DisplayName("[error] 재고 부족 시 500 코드와 에러 메시지를 응답한다.")
        void error_outOfStock() throws Exception {
            // given
            List<ReserveProductRequest> request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.reservation(any())).willThrow(
                new CustomGrpcResponseError("OUT_OF_STOCK - 재고가 부족합니다 - 1"));

            // when, then
            performDocument(request, authorization, status().isInternalServerError(),
                "재고 부족", "error",
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
        @DisplayName("[error] 이미 예약된 상품일 때 500 코드와 에러 메시지를 응답한다.")
        void error_alreadyReserved() throws Exception {
            // given
            List<ReserveProductRequest> request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.reservation(any())).willThrow(
                new CustomGrpcResponseError("ALREADY_RESERVED - 이미 예약된 상품입니다 - 1"));

            // when, then
            performDocument(request, authorization, status().isInternalServerError(),
                "이미 예약된 상품", "error",
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
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 예약 결과를 응답한다.")
        void success() throws Exception {
            // given
            List<ReserveProductRequest> request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.reservation(any()))
                .willReturn(List.of(
                    ReserveProductServiceResponse.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build(),
                    ReserveProductServiceResponse.builder()
                        .productId(2L)
                        .reserveId(101L)
                        .build()
                ));

            // when then
            performDocument(request, authorization, status().isOk(),
                "success", apiName,
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.ARRAY)
                    .description("응답 데이터"),
                fieldWithPath("data[].productId").type(JsonFieldType.NUMBER)
                    .description("상품 ID"),
                fieldWithPath("data[].reserveId").type(JsonFieldType.NUMBER)
                    .description("예약 ID")
            );
        }
    }

    private List<ReserveProductRequest> createValidRequest() {
        return List.of(
            ReserveProductRequest.builder()
                .productId(1L)
                .quantity(2L)
                .build(),
            ReserveProductRequest.builder()
                .productId(2L)
                .quantity(1L)
                .build()
        );
    }

    private void performDocument(List<ReserveProductRequest> request,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        JsonFieldType productIdType = JsonFieldType.NUMBER;
        JsonFieldType quantityType = JsonFieldType.NUMBER;

        if (!request.isEmpty()) {
            ReserveProductRequest firstRequest = request.get(0);
            productIdType = firstRequest.productId() == null ? JsonFieldType.NULL : JsonFieldType.NUMBER;
            quantityType = firstRequest.quantity() == null ? JsonFieldType.NULL : JsonFieldType.NUMBER;
        }

        mockMvc.perform(post("/orders/reservation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", authorization))
            .andDo(print())
            .andExpect(status)
            .andDo(document(String.format("[%s] %s", apiName, docIdentifier),
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Order")
                        .summary("상품 예약 API")
                        .description("주문 전 상품을 예약하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestFields(
                            fieldWithPath("[].productId").type(productIdType)
                                .description("상품 ID (필수)"),
                            fieldWithPath("[].quantity").type(quantityType)
                                .description("상품 수량 (필수)")
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] " + apiName))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(List<ReserveProductRequest> request,
        String authorization,
        ResultMatcher status, String identifier) throws Exception {
        performDocument(request, authorization, status, identifier, "error",
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
