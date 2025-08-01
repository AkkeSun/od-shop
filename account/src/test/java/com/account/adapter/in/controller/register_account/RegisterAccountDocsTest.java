package com.account.adapter.in.controller.register_account;

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
import com.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.applicaiton.service.register_account.RegisterAccountServiceResponse;
import com.account.infrastructure.exception.CustomValidationException;
import com.account.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class RegisterAccountDocsTest extends RestDocsSupport {

    private final RegisterAccountUseCase registerAccountUseCase =
        mock(RegisterAccountUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterAccountController(registerAccountUseCase);
    }

    @Nested
    @DisplayName("[registerAccount] 사용자 정보를 등록하는 API")
    class Describe_registerAccount {

        @Test
        @DisplayName("[success] 필수값을 모두 입력했을 때 200코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("od@test.com")
                .password("1234")
                .passwordCheck("1234")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when // then
            performDocument(request, status().isOk(),
                "[register-account] success",
                "[response] register-account",
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

        @DisplayName("[error] 이메일을 빈 값으로 입력했을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("")
                .password("1234")
                .passwordCheck("1234")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 이메일 미입력");
        }

        @Test
        @DisplayName("[error] 비밀번호를 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("")
                .passwordCheck("1234")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 비밀번호 미입력");
        }

        @Test
        @DisplayName("[error] 비밀번호 확인을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error5() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 비밀번호 확인 미입력");
        }

        @Test
        @DisplayName("[error] 권한을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error7() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 권한 미입력");
        }

        @Test
        @DisplayName("[error] 비밀번호와 비밀번호 확인이 동일하지 않을 때 400 코드와 오류 메시지를 응답한다.")
        void error9() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("12345")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            RegisterAccountServiceResponse response = RegisterAccountServiceResponse.builder()
                .accessToken("accessToken")
                .refreshToken("refreshToken")
                .build();
            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willReturn(response);

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 비밀번호와 비밀번호 확인 미일치");
        }

        @Test
        @DisplayName("[error] 유효한 권한을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error10() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .roles(List.of("ERROR"))
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();

            given(registerAccountUseCase.registerAccount(request.toCommand()))
                .willThrow(new CustomValidationException(ErrorCode.INVALID_ROLE));

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 유효하지 않는 권한 입력");
        }

        @Test
        @DisplayName("[error] 올바른 전화번호 형식을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error11() throws Exception {
            // given
            RegisterAccountRequest request = RegisterAccountRequest.builder()
                .email("test@gmail.com")
                .password("1234")
                .passwordCheck("1234")
                .roles(List.of("ROLE_CUSTOMER"))
                .username("od")
                .userTel("111")
                .address("서울시 강남구")
                .build();

            // when // then
            performErrorDocument(request, status().isBadRequest(),
                "[register-account] 유효하지 않는 전화번호 패턴 입력");
        }

        private void performDocument(RegisterAccountRequest request,
            ResultMatcher status, String docIdentifier, String responseSchema,
            FieldDescriptor... responseFields) throws Exception {
            JsonFieldType roleType = request.getRoles() == null ?
                JsonFieldType.NULL : JsonFieldType.ARRAY;

            mockMvc.perform(post("/accounts")
                    .content(objectMapper.writeValueAsString(request))
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status)
                .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Account")
                            .summary("사용자 등록 API")
                            .description("사용자를 등록하는 API 입니다.")
                            .requestFields(
                                fieldWithPath("email").type(JsonFieldType.STRING)
                                    .description("이메일 (50자 미만)"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호"),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인"),
                                fieldWithPath("roles").type(roleType)
                                    .description("권한 (ROLE_CUSTOMER, ROLE_SELLER)"),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름 (10자 미만)").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호 (01012341234 패턴)").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소 (100자 미만)").optional()
                            )
                            .responseFields(responseFields)
                            .requestSchema(Schema.schema("[request] register-account"))
                            .responseSchema(Schema.schema(responseSchema))
                            .build()
                        )
                    )
                );
        }

        private void performErrorDocument(RegisterAccountRequest request, ResultMatcher status,
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
}