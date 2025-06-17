package com.account.adapter.in.find_account;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.RestDocsSupport;
import com.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.applicaiton.service.find_account.FindAccountServiceResponse;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.CustomNotFoundException;
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

class FindAccountDocsTest extends RestDocsSupport {

    private final FindAccountInfoUseCase findAccountInfoUseCase =
        mock(FindAccountInfoUseCase.class);

    @Override
    protected Object initController() {
        return new FindAccountController(findAccountInfoUseCase);
    }

    @Nested
    @DisplayName("[FindAccountInfo] 사용자 정보를 조회하는 API")
    class Describe_FindAccountInfo {

        @Test
        @DisplayName("[success] 권한이 있는 사용자가 API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer test-success-token";
            FindAccountServiceResponse response = FindAccountServiceResponse.builder()
                .id(1L)
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .email("test@google.com")
                .role("ROLE_CUSTOMER")
                .regDate("20241212")
                .build();
            given(findAccountInfoUseCase.findAccountInfo(any())).willReturn(response);

            // when then
            performDocument(authorization,
                status().isOk(),
                "[find-account-info] success",
                "[response] find-account-info",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                    .description("사용자 아이디"),
                fieldWithPath("data.username").type(JsonFieldType.STRING)
                    .description("사용자 이름"),
                fieldWithPath("data.userTel").type(JsonFieldType.STRING)
                    .description("사용자 전화번호"),
                fieldWithPath("data.address").type(JsonFieldType.STRING)
                    .description("사용자 주소"),
                fieldWithPath("data.email").type(JsonFieldType.STRING)
                    .description("사용자 이메일"),
                fieldWithPath("data.role").type(JsonFieldType.STRING)
                    .description("사용자 권한"),
                fieldWithPath("data.regDate").type(JsonFieldType.STRING)
                    .description("등록일")
            );
        }

        @Test
        @DisplayName("[error] 권한이 없는 사용자가 API 를 호출했을 401 코드와 오류 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(findAccountInfoUseCase.findAccountInfo(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performErrorDocument(authorization, status().isUnauthorized(),
                "[find-account-info] 유효하지 않은 토큰 입력");
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 404 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            String authorization = "Bearer test-success-token";
            given(findAccountInfoUseCase.findAccountInfo(any())).willThrow(
                new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when // then
            performErrorDocument(authorization, status().isNotFound(),
                "[find-account-info] 조회된 사용자 정보 없음");
        }
    }

    private void performDocument(String accessToken,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(get("/accounts")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Account")
                        .summary("사용자 정보 조회 API")
                        .description("인증토큰으로 사용자 정보를 조회하는 API 입니다.")
                        .requestHeaders(
                            headerWithName("Authorization").description("인증 토큰")
                        )
                        .responseFields(responseFields)
                        .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }

    private void performErrorDocument(String accessToken, ResultMatcher status, String identifier)
        throws Exception {

        performDocument(accessToken, status, identifier, "[response] error",
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