package com.account.adapter.in.delete_account;

import static com.account.infrastructure.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.ControllerTestSupport;
import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import com.account.domain.model.Account;
import com.account.infrastructure.exception.ErrorResponse;
import com.account.infrastructure.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class DeleteAccountControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 API")
    class Describe_deleteAccount {

        @Test
        @WithMockUser(username = "od", roles = "CUSTOMER")
        @DisplayName("[success] 권한 정보가 있는 사용자가 API 를 요청한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String accessToken = "test-access-token";
            DeleteAccountServiceResponse response = DeleteAccountServiceResponse.builder()
                .id(10L)
                .result("Y")
                .build();
            given(deleteAccountUseCase.deleteAccount(new Account()))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(delete("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.result").value(response.result()))
                .andDo(print());
        }

        @Test
        @WithAnonymousUser
        @DisplayName("[success] 권한 정보가 없는 사용자가 API 를 호출한 경우 401 코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String accessToken = "test-error-token";
            ApiResponse<ErrorResponse> errorResponseApiResponse = ApiResponse.of(
                HttpStatus.UNAUTHORIZED,
                ErrorResponse.builder()
                    .errorCode(INVALID_ACCESS_TOKEN.getCode())
                    .errorMessage(INVALID_ACCESS_TOKEN.getMessage())
                    .build());

            // when
            ResultActions actions = mockMvc.perform(delete("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
            );

            // then
            actions.andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.httpStatus").value(401))
                .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.data.errorCode").value(INVALID_ACCESS_TOKEN.getCode()))
                .andExpect(jsonPath("$.data.errorMessage").value(INVALID_ACCESS_TOKEN.getMessage()))
                .andDo(print());
        }
    }
}