package com.account.adapter.in.update_account;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
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
import com.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import com.account.infrastructure.exception.CustomAuthenticationException;
import com.account.infrastructure.exception.CustomNotFoundException;
import com.account.infrastructure.exception.ErrorCode;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultMatcher;


class UpdateAccountDocsTest extends RestDocsSupport {

    private final UpdateAccountUseCase updateAccountUseCase
        = mock(UpdateAccountUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateAccountController(updateAccountUseCase);
    }

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 권한 정보가 있는 사용자가 요청 정보를 올바르게 입력한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01022222323")
                .address("서울시 강남구")
                .build();
            String accessToken = "test-access-token";
            UpdateAccountServiceResponse response = UpdateAccountServiceResponse.builder()
                .updateYn("Y")
                .updateList(Collections.singletonList("username"))
                .build();
            given(updateAccountUseCase.updateAccount(any()))
                .willReturn(response);

            // when // then
            performPatchAndDocument(request, accessToken, status().isOk(),
                "[updateAccount] SUCCESS", "[RESPONSE] update-account",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.updateYn").type(JsonFieldType.STRING)
                    .description("업데이트 유무 (Y, N)"),
                fieldWithPath("data.updateList").type(JsonFieldType.ARRAY)
                    .description("업데이트 목록")
            );
        }


        @Test
        @DisplayName("[success] 권한 정보가 없는 사용자가 API 를 호출한 경우 401 코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("111")
                .passwordCheck("111")
                .username("od")
                .userTel("01012341234")
                .address("서울시 강남구")
                .build();
            String authorization = "Bearer invalid-token";
            given(updateAccountUseCase.updateAccount(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when // then
            performPatchAndDocument(request, authorization, status().isUnauthorized(),
                "[updateAccount] 유효하지 않은 토큰 입력", "[RESPONSE] ERROR",
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
        @DisplayName("[error] 비밀번호와 비밀번호 확인이 동일하지 않을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("12345")
                .username("od")
                .userTel("01022222323")
                .address("서울시 강남구")
                .build();
            String accessToken = "test-access-token";

            // when // then
            performPatchAndDocument(request, accessToken, status().isBadRequest(),
                "[updateAccount] 비밀번호와 비밀번호 확인 불일치", "[RESPONSE] ERROR",
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
        @DisplayName("[error] 올바른 전화번호 형식을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("010123")
                .address("서울시 강남구")
                .build();
            String accessToken = "test-access-token";

            // when // then
            performPatchAndDocument(request, accessToken, status().isBadRequest(),
                "[updateAccount] 유효하지 않은 전화번호 형식 입력", "[RESPONSE] ERROR",
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
        void error4() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012341234")
                .address("서울시 강남구")
                .build();
            String accessToken = "access-token-2";
            given(updateAccountUseCase.updateAccount(any()))
                .willThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when // then
            performPatchAndDocument(request, accessToken, status().isNotFound(),
                "[updateAccount] 조회된 사용자 정보 없음", "[RESPONSE] ERROR",
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

        private void performPatchAndDocument(UpdateAccountRequest request, String accessToken,
            ResultMatcher status, String docIdentifier, String responseSchema,
            FieldDescriptor... responseFields) throws Exception {

            mockMvc.perform(put("/accounts")
                    .content(objectMapper.writeValueAsString(request))
                    .header("Authorization", accessToken)
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status)
                .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag("Account")
                            .summary("사용자 수정 API")
                            .description("사용자 정보를 수정하는 API 입니다. <br>"
                                + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                                + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                            .requestFields(
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("사용자 이름"),
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호"),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인"),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호 (01012341234 패턴)").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소 (100)").optional()
                            )
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .responseFields(responseFields)
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema(responseSchema))
                            .build()
                        )
                    )
                );
        }
    }
}