package com.account.adapter.in.controller.update_token;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.account.ControllerTestSupport;
import com.account.applicaiton.service.update_token.UpdateTokenServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.ResultActions;

class UpdateTokenControllerTest extends ControllerTestSupport {

    @Nested
    @WithAnonymousUser
    @DisplayName("[registerToken] 리프래시 토큰을 통해 토큰을 갱신하는 API")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .refreshToken("test-refresh-token")
                .build();
            UpdateTokenServiceResponse response =
                UpdateTokenServiceResponse.builder()
                    .accessToken("new-access-token")
                    .refreshToken("new-refresh-token")
                    .build();
            given(registerTokenByRefreshUseCase.update(
                request.getRefreshToken())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(put("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(response.refreshToken()))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .build();

            // when
            ResultActions actions = mockMvc.perform(put("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1099))
                .andExpect(jsonPath("$.data.errorMessage").value("리프레시 토큰은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 빈 값으로 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateTokenRequest request = UpdateTokenRequest
                .builder()
                .refreshToken("")
                .build();

            // when
            ResultActions actions = mockMvc.perform(put("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1099))
                .andExpect(jsonPath("$.data.errorMessage").value("리프레시 토큰은 필수값 입니다."))
                .andDo(print());
        }
    }
}