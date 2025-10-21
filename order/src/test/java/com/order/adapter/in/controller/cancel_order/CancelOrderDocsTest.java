package com.order.adapter.in.controller.cancel_order;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.order.RestDocsSupport;
import com.order.application.port.in.CancelOrderUseCase;
import com.order.application.service.cancel_order.CancelOrderServiceResponse;
import com.common.infrastructure.exception.CustomAuthenticationException;
import com.common.infrastructure.exception.CustomBusinessException;
import com.common.infrastructure.exception.CustomNotFoundException;
import com.common.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class CancelOrderDocsTest extends RestDocsSupport {

    String apiName = "cancel_order";
    CancelOrderUseCase useCase = mock(CancelOrderUseCase.class);

    @Override
    protected Object initController() {
        return new CancelOrderController(useCase);
    }

    @Nested
    @DisplayName("[cancel] 주문을 취소하는 API")
    class Describe_cancel {

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            Long orderId = 1L;
            String authorization = "testToken";
            given(useCase.cancel(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN_BY_SECURITY));
            // when, then
            performErrorDocument(orderId, authorization, status().isUnauthorized(),
                "인증 토큰 미입력 혹은 만료된 토큰 입력");
        }

        @Test
        @DisplayName("[error] 조회된 주문이 없는경우 404 코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            Long orderId = 1L;
            String authorization = "testToken";
            given(useCase.cancel(any())).willThrow(
                new CustomNotFoundException(ErrorCode.DoesNotExist_Order));
            // when, then
            performErrorDocument(orderId, authorization, status().isNotFound(),
                "조회된 주문 없음");
        }

        @Test
        @DisplayName("[error] 조회된 주문의 구매자가 아닌 경우 500 코드와 에러 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            Long orderId = 1L;
            String authorization = "testToken";
            given(useCase.cancel(any())).willThrow(
                new CustomBusinessException(ErrorCode.Business_NO_CUSTOMER));

            // when, then
            performErrorDocument(orderId, authorization, status().isInternalServerError(),
                "주문한 사용자가 아님");
        }

        @Test
        @DisplayName("[error] 이미 취소된 상품의 경우 500 코드와 에러 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            Long orderId = 1L;
            String authorization = "testToken";
            given(useCase.cancel(any())).willThrow(
                new CustomBusinessException(ErrorCode.Business_ALREADY_CANCEL_ORDCER));

            // when, then
            performErrorDocument(orderId, authorization, status().isInternalServerError(),
                "이미 취소처리된 상품");
        }

        @Test
        @DisplayName("[success] 필수값을 모두 입력했을 때 200 코드와 공지사항 목록을 응답한다.")
        void success() throws Exception {
            // given
            Long orderId = 1L;
            String authorization = "Bearer testToken";
            given(useCase.cancel(any()))
                .willReturn(CancelOrderServiceResponse.ofSuccess());

            // when then
            performDocument(orderId, authorization, status().isOk(),
                "success", apiName,
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                    .description("처리 결과")
            );
        }
    }


    private void performDocument(Long orderId,
        String authorization, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(delete("/orders/{orderId}", orderId)
                .header("Authorization", authorization))
            .andDo(print())
            .andExpect(status)
            .andDo(document(String.format("[%s] %s", apiName, docIdentifier),
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Order")
                        .summary("주문 취소 API")
                        .description("주문을 취소하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] " + apiName))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(Long orderId,
        String accessToke,
        ResultMatcher status, String identifier) throws Exception {
        performDocument(orderId, accessToke, status, identifier, "error",
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