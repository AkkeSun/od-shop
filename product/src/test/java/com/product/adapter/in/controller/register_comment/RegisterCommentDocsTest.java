package com.product.adapter.in.controller.register_comment;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import com.product.application.port.in.RegisterCommentUseCase;
import com.product.application.service.register_comment.RegisterCommentServiceResponse;
import com.product.infrastructure.exception.CustomAuthenticationException;
import com.product.infrastructure.exception.CustomAuthorizationException;
import com.product.infrastructure.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

public class RegisterCommentDocsTest extends RestDocsSupport {

    RegisterCommentUseCase registerCommentUseCase =
        mock(RegisterCommentUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterCommentController(registerCommentUseCase);
    }

    @Nested
    @DisplayName("[registerComment] 리뷰를 등록하는 API")
    class Describe_registerComment {

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 필수값을 모두 입력하여 API 를 요청 했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트 리뷰 정보 입니다.")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            when(registerCommentUseCase.registerComment(any()))
                .thenReturn(RegisterCommentServiceResponse.ofSuccess());

            // when then
            performAndDocument(request, authorization, productId, status().isOk(),
                "success", "register-product",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.BOOLEAN)
                    .description("성공 여부")
            );
        }

        @Test
        @DisplayName("[error] 인증받지 않은 사용자가 API 를 요청 했을 때 401코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트 리뷰 정보 입니다.")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            given(registerCommentUseCase.registerComment(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when the
            performErrorDocument(request, authorization, productId,
                status().isUnauthorized(), "인증받지 않은 사용자 요청");
        }

        @Test
        @DisplayName("[error] 등록 권한이 없는 사용자가 API 를 요청 했을 때 403코드와 에러 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트 리뷰 정보 입니다.")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;
            given(registerCommentUseCase.registerComment(any())).willThrow(
                new CustomAuthorizationException(ErrorCode.ACCESS_DENIED));

            // when then
            performErrorDocument(request, authorization, productId,
                status().isForbidden(), "상품 등록 권한이 없는 사용자 요청");
        }

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 리뷰를 빈 값으로 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment(null)
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;

            // when then
            performErrorDocument(request, authorization, productId,
                status().isBadRequest(), "리뷰 미입력");
        }

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 리뷰를 빈 문자열로 입력했을 때 400코드와 성공 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;

            // when then
            performErrorDocument(request, authorization, productId,
                status().isBadRequest(), "리뷰 빈 값 입력");
        }

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 유효한 리뷰 사이즈를 입력하지 않았을 때 400코드와 성공 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("error")
                .score(5.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;

            // when then
            performErrorDocument(request, authorization, productId,
                status().isBadRequest(), "잘못된 길이의 리뷰 입력");
        }

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 점수를 입력하지 않았을 때 400코드와 성공 메시지를 응답한다.")
        void error6() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트 리뷰 정보 입니다.")
                .score(null)
                .build();
            String authorization = "testToken";
            Long productId = 1L;

            // when then
            performErrorDocument(request, authorization, productId,
                status().isBadRequest(), "점수 미입력");
        }

        @Test
        @DisplayName("[success] 등록 권한을 가진 사용자가 유효한 점수를 입력하지 않았을 때 400코드와 성공 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterCommentRequest request = RegisterCommentRequest.builder()
                .comment("테스트 리뷰 정보 입니다.")
                .score(7.0)
                .build();
            String authorization = "testToken";
            Long productId = 1L;

            // when then
            performErrorDocument(request, authorization, productId,
                status().isBadRequest(), "유효하지 않은 점수 입력");
        }
    }

    private void performAndDocument(RegisterCommentRequest request, String authorization,
        Long productId, ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields)
        throws Exception {

        JsonFieldType scoreType =
            request.score() == null ? JsonFieldType.NULL : JsonFieldType.NUMBER;
        JsonFieldType commentType =
            request.comment() == null ? JsonFieldType.NULL : JsonFieldType.STRING;

        mockMvc.perform(post("/products/{productId}/comments", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", authorization)
            )
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document("[register-comment] " + docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Comment")
                        .summary("리뷰 등록 API")
                        .description("리뷰를 등록하는 API 입니다. <br><br>"
                            + "1. 테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. (요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다) <br>"
                            + "2. 해당 상품을 구매한 사용자만 리뷰를 등록할 수 있습니다.")
                        .requestFields(
                            fieldWithPath("comment").type(commentType)
                                .description("리뷰 (10자 이상 20자 이하)"),
                            fieldWithPath("score").type(scoreType)
                                .description("점수 (0.5 이상 5.0 이하)")
                        )
                        .pathParameters(
                            parameterWithName("productId").description("상품 ID")
                        )
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] register-comment"))
                        .responseSchema(Schema.schema("[response] " + responseSchema))
                        .build())
                )
            );
    }

    private void performErrorDocument(RegisterCommentRequest request, String authorization,
        Long productId, ResultMatcher status, String identifier)
        throws Exception {

        performAndDocument(request, authorization, productId, status, identifier, "error",
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
