package com.account.adapter.in.controller.register_token;

import static com.account.infrastructure.exception.ErrorCode.DoesNotExist_ACCOUNT_INFO;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.RestDocsSupport;
import com.account.applicaiton.port.in.RegisterTokenUseCase;
import com.account.applicaiton.service.register_token.RegisterTokenServiceResponse;
import com.account.infrastructure.exception.CustomNotFoundException;
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

class RegisterTokenDocsTest extends RestDocsSupport {

    private final RegisterTokenUseCase registerTokenUseCase =
        mock(RegisterTokenUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterTokenController(registerTokenUseCase);
    }

    @Nested
    @DisplayName("[registerToken] 사용자 토큰을 등록하는 API")
    class Describe_RegisterToken {

        @Test
        @DisplayName("[success] 필수값을 올바르게 입력했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();
            RegisterTokenServiceResponse response = RegisterTokenServiceResponse.builder()
                .accessToken("Bearer testAccessToken")
                .refreshToken("Bearer testRefreshToken")
                .build();
            given(registerTokenUseCase.registerToken(request.toCommand()))
                .willReturn(response);

            // when // then
            performDocument(request, status().isOk(),
                "[register-token] success",
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
                    .description("리프레시 토큰")
            );
        }


        @Test
        @DisplayName("[error] 이메일을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("")
                .password("1234")
                .build();

            // when // then
            performErrorDocument(request, status().is4xxClientError(),
                "[register-token] 이메일 미입력");
        }

        @Test
        @DisplayName("[error] 비밀번호를 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("")
                .build();

            // when // then
            performErrorDocument(request, status().is4xxClientError(),
                "[register-token] 비밀번호 미입력");
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없을 때 404 코드와 오류 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterTokenRequest request = RegisterTokenRequest.builder()
                .email("test@gmail.com")
                .password("1111111")
                .build();
            given(registerTokenUseCase.registerToken(request.toCommand()))
                .willThrow(new CustomNotFoundException(DoesNotExist_ACCOUNT_INFO));

            // when // then
            performErrorDocument(request, status().is4xxClientError(),
                "[register-token] 조회된 사용자 정보 없음");
        }
    }

    private void performDocument(RegisterTokenRequest request,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(post("/auth")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Token")
                        .summary("인증 토큰 발급 API")
                        .description("이메일과 비밀번호로 인증 토큰을 발급하는 API 입니다. <br>"
                            + "인증토큰 유효기간은 10분 이며 리프레시 토큰 유효기간은 3일 입니다.")
                        .requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("이메일"),
                            fieldWithPath("password").type(JsonFieldType.STRING)
                                .description("비밀번호")
                        )
                        .responseFields(responseFields)
                        .requestSchema(Schema.schema("[request] register-token"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(RegisterTokenRequest request, ResultMatcher status,
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