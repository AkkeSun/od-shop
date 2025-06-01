package com.account.adapter.in.delete_account;

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

import com.account.RestDocsSupport;
import com.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;

class DeleteAccountDocsTest extends RestDocsSupport {

    private final DeleteAccountUseCase deleteAccountUseCase
        = mock(DeleteAccountUseCase.class);

    @Override
    protected Object initController() {
        return new DeleteAccountController(deleteAccountUseCase);
    }

    private void performPatchAndDocument(String accessToken,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(delete("/accounts")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status)
            .andDo(document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Account")
                        .summary("사용자 정보 삭제 API")
                        .description("사용자를 삭제하는 API 입니다. <br><br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
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

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 API")
    class Describe_deleteAccount {

        @Test
        @DisplayName("[success] 권한 정보가 있는 사용자가 API 를 요청한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String accessToken = "test-access-token";
            DeleteAccountServiceResponse response = DeleteAccountServiceResponse.builder()
                .id(10L)
                .result("Y")
                .build();
            given(deleteAccountUseCase.deleteAccount(any()))
                .willReturn(response);

            // when // then
            performPatchAndDocument(accessToken, status().isOk(),
                "[deleteAccount] SUCCESS",
                "[RESPONSE] delete-account",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                    .description("사용자 아이디"),
                fieldWithPath("data.result").type(JsonFieldType.STRING)
                    .description("삭제 유무")
            );
        }


        @Test
        @DisplayName("[success] 권한 정보가 없는 사용자가 API 를 호출한 경우 401 코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(deleteAccountUseCase.deleteAccount(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performPatchAndDocument(authorization,
                status().isUnauthorized(),
                "[deleteAccount] 유효하지 않은 토큰 입력",
                "[RESPONSE] ERROR",
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
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 404 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            String accessToken = "test-access-token";
            given(deleteAccountUseCase.deleteAccount(any()))
                .willThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when // then
            performPatchAndDocument(accessToken,
                status().isNotFound(),
                "[deleteAccount] 조회된 사용자 정보 없음",
                "[RESPONSE] ERROR",
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