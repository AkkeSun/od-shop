package com.account.adapter.in.controller.update_token;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.RestDocsSupport;
import com.account.applicaiton.port.in.UpdateTokenUseCase;
import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class UpdateTokenDocsTest extends RestDocsSupport {

    private final UpdateTokenUseCase registerTokenByRefreshUseCase
        = mock(UpdateTokenUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateTokenController(registerTokenByRefreshUseCase);
    }

    @Nested
    @DisplayName("[registerToken] 리프래시 토큰을 통해 사용자 토큰을 갱신하는 API")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .refreshToken("valid-refresh-token")
                .build();
            UpdateTokenServiceResponse response =
                UpdateTokenServiceResponse.builder()
                    .accessToken("new-access-token")
                    .refreshToken("new-refresh-token")
                    .build();
            given(registerTokenByRefreshUseCase.update(
                request.getRefreshToken())).willReturn(response);

            // when // then
            performDocument(request, status().isOk(),
                "[refresh-token] success",
                "[response] register-token",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                    .description("액세스 토큰"),
                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                    .description("리프레시 토큰"));
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 빈 값으로 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .refreshToken("")
                .build();

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[refresh-token] 리프레시 토큰 미입력");
        }

        @Test
        @DisplayName("[error] 유효하지 않은 토큰을 입력했을 때 401 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .refreshToken("invalid-refresh-token")
                .build();
            given(registerTokenByRefreshUseCase.update(request.getRefreshToken()))
                .willThrow(new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN));

            // when // then
            performErrorDocument(request, status().isUnauthorized(),
                "[refresh-token] 유효하지 않은 리프레시 토큰 입력");
        }
    }

    private void performDocument(UpdateTokenRequest request,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(put("/auth")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Token")
                        .summary("인증 토큰 갱신 API")
                        .description("리프래시 토큰으로 인증 토큰을 갱신하는 API 입니다. <br> "
                            + "리프레시 토큰을 발급받은 기기와 다른 기기에서 시도하거나 리프레시토큰이 만료된 경우 갱신에 실패합니다.")
                        .requestFields(
                            fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                .description("리프레시 토큰")
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] refresh-token"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(UpdateTokenRequest request, ResultMatcher status,
        String identifier) throws Exception {

        performDocument(request, status, identifier, "[response] error",
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