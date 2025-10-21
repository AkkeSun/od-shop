package com.order.adapter.in.controller.register_order;

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
import com.order.application.port.in.RegisterOrderUseCase;
import com.order.application.service.register_order.RegisterOrderServiceResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class RegisterOrderDocsTest extends RestDocsSupport {

    String apiName = "register_order";
    RegisterOrderUseCase useCase = mock(RegisterOrderUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterOrderController(useCase);
    }

    @Nested
    @DisplayName("[register] 주문을 등록하는 API")
    class Describe_register {

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API를 요청했을 때 401 코드와 에러 메시지를 응답한다.")
        void error_unauthorized() throws Exception {
            // given
            RegisterOrderRequest request = createValidRequest();
            String authorization = "testToken";
            given(useCase.register(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));

            // when, then
            performErrorDocument(request, authorization, status().isUnauthorized(),
                "인증 토큰 미입력 혹은 만료된 토큰 입력");
        }

        @Test
        @DisplayName("[error] reserveInfos가 null일 때 400 코드와 에러 메시지를 응답한다.")
        void error_reserveInfos_null() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(null)
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "예약 정보 미입력");
        }

        @Test
        @DisplayName("[error] reserveInfos가 빈 리스트일 때 400 코드와 에러 메시지를 응답한다.")
        void error_reserveInfos_empty() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of())
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "예약 정보 미입력");
        }

        @Test
        @DisplayName("[error] totalPrice가 null일 때 400 코드와 에러 메시지를 응답한다.")
        void error_totalPrice_null() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(null)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "총 금액 미입력");
        }

        @Test
        @DisplayName("[error] receiverName이 null일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverName_null() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName(null)
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 이름 미입력");
        }

        @Test
        @DisplayName("[error] receiverName이 빈 문자열일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverName_blank() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName("   ")
                .receiverTel("010-1234-5678")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 이름 미입력");
        }

        @Test
        @DisplayName("[error] receiverTel이 null일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverTel_null() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel(null)
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 전화번호 미입력");
        }

        @Test
        @DisplayName("[error] receiverTel이 빈 문자열일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverTel_blank() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("   ")
                .receiverAddress("서울시 강남구 테헤란로 123")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 전화번호 미입력");
        }

        @Test
        @DisplayName("[error] receiverAddress가 null일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverAddress_null() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress(null)
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 주소 미입력");
        }

        @Test
        @DisplayName("[error] receiverAddress가 빈 문자열일 때 400 코드와 에러 메시지를 응답한다.")
        void error_receiverAddress_blank() throws Exception {
            // given
            RegisterOrderRequest request = RegisterOrderRequest.builder()
                .reserveInfos(List.of(
                    RegisterOrderRequest.RegisterOrderRequestItem.builder()
                        .productId(1L)
                        .reserveId(100L)
                        .build()
                ))
                .totalPrice(50000)
                .receiverName("홍길동")
                .receiverTel("010-1234-5678")
                .receiverAddress("   ")
                .build();
            String authorization = "Bearer testToken";

            // when, then
            performErrorDocument(request, authorization, status().isBadRequest(),
                "받는 사람 주소 미입력");
        }

        @Test
        @DisplayName("[error] 예약 확정 실패 시 500 코드와 에러 메시지를 응답한다.")
        void error_reserveConfirmFailed() throws Exception {
            // given
            RegisterOrderRequest request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.register(any())).willThrow(
                new CustomGrpcResponseError("ALREADY_CONFIRMED - 이미 확정된 예약입니다 - 1"));

            // when, then
            performDocument(request, authorization, status().isInternalServerError(),
                "예약 확정 실패", "error",
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
        @DisplayName("[error] 예약 정보가 존재하지 않을 때 500 코드와 에러 메시지를 응답한다.")
        void error_reserveNotFound() throws Exception {
            // given
            RegisterOrderRequest request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.register(any())).willThrow(
                new CustomGrpcResponseError("NOT_FOUND - 예약 정보를 찾을 수 없습니다 - 100"));

            // when, then
            performDocument(request, authorization, status().isInternalServerError(),
                "예약 정보 없음", "error",
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
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 주문 결과를 응답한다.")
        void success() throws Exception {
            // given
            RegisterOrderRequest request = createValidRequest();
            String authorization = "Bearer testToken";
            given(useCase.register(any()))
                .willReturn(RegisterOrderServiceResponse.builder()
                    .result(true)
                    .orderNumber(12345L)
                    .build());

            // when then
            performDocument(request, authorization, status().isOk(),
                "success", apiName,
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                    .description("주문 처리 결과"),
                fieldWithPath("data.orderNumber").type(JsonFieldType.NUMBER)
                    .description("주문 번호")
            );
        }
    }

    private RegisterOrderRequest createValidRequest() {
        return RegisterOrderRequest.builder()
            .reserveInfos(List.of(
                RegisterOrderRequest.RegisterOrderRequestItem.builder()
                    .productId(1L)
                    .reserveId(100L)
                    .build(),
                RegisterOrderRequest.RegisterOrderRequestItem.builder()
                    .productId(2L)
                    .reserveId(101L)
                    .build()
            ))
            .totalPrice(50000)
            .receiverName("홍길동")
            .receiverTel("010-1234-5678")
            .receiverAddress("서울시 강남구 테헤란로 123")
            .build();
    }

    private void performDocument(RegisterOrderRequest request,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        JsonFieldType reserveInfosType =
            request.reserveInfos() == null ? JsonFieldType.NULL : JsonFieldType.ARRAY;
        JsonFieldType totalPriceType =
            request.totalPrice() == null ? JsonFieldType.NULL : JsonFieldType.NUMBER;
        JsonFieldType receiverNameType =
            request.receiverName() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType receiverTelType =
            request.receiverTel() == null ? JsonFieldType.NULL : JsonFieldType.STRING;
        JsonFieldType receiverAddressType =
            request.receiverAddress() == null ? JsonFieldType.NULL : JsonFieldType.STRING;

        mockMvc.perform(post("/orders")
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
                        .summary("주문 등록 API")
                        .description("상품 예약 정보를 기반으로 주문을 등록하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .requestFields(
                            fieldWithPath("reserveInfos").type(reserveInfosType)
                                .description("예약 정보 목록")
                                .optional(),
                            fieldWithPath("reserveInfos[].productId").type(JsonFieldType.NUMBER)
                                .description("상품 ID")
                                .optional(),
                            fieldWithPath("reserveInfos[].reserveId").type(JsonFieldType.NUMBER)
                                .description("예약 ID")
                                .optional(),
                            fieldWithPath("totalPrice").type(totalPriceType)
                                .description("총 금액"),
                            fieldWithPath("receiverName").type(receiverNameType)
                                .description("받는 사람 이름"),
                            fieldWithPath("receiverTel").type(receiverTelType)
                                .description("받는 사람 전화번호"),
                            fieldWithPath("receiverAddress").type(receiverAddressType)
                                .description("받는 사람 주소")
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] " + apiName))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(RegisterOrderRequest request,
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
