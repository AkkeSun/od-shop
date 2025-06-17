package com.account.adapter.in.delete_token;

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
import com.account.applicaiton.port.in.DeleteTokenUseCase;
import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultMatcher;


class DeleteTokenDocsTest extends RestDocsSupport {

    private final DeleteTokenUseCase deleteTokenUseCase =
        mock(DeleteTokenUseCase.class);

    @Override
    protected Object initController() {
        return new DeleteTokenController(deleteTokenUseCase);
    }

    @Nested
    @WithAnonymousUser
    @DisplayName("[deleteToken] 토큰을 삭제하는 API")
    class Describe_deleteToken {

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {

            // given
            String accessToken = "test-access-token";
            DeleteTokenServiceResponse response = DeleteTokenServiceResponse.builder()
                .result("Y")
                .build();
            given(deleteTokenUseCase.deleteToken(any())).willReturn(response);

            // when
            performDocument(accessToken, status().isOk(),
                "[deleteToken] success",
                "[response] delete-token",
                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                    .description("상태 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("상태 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.result").type(JsonFieldType.STRING)
                    .description("결과")
            );
        }
    }

    private void performDocument(String accessToken,
        ResultMatcher status, String docIdentifier, String responseSchema,
        FieldDescriptor... responseFields) throws Exception {

        mockMvc.perform(delete("/auth")
                .header("Authorization", accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status)
            .andDo(MockMvcRestDocumentationWrapper.document(docIdentifier,
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    resource(ResourceSnippetParameters.builder()
                        .tag("Token")
                        .summary("토큰 삭제 API")
                        .description("리프레시 토큰을 삭제하는 API 입니다. <br>"
                            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
                            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)")
                        .requestHeaders(
                            headerWithName("Authorization").description("인증 토큰")
                        )
                        .responseFields(responseFields)
                        .responseSchema(Schema.schema(responseSchema))
                        .build()
                    )
                )
            );
    }
}
