package com.account;

import com.account.account.applicaiton.port.in.DeleteAccountUseCase;
import com.account.account.applicaiton.port.in.FindAccountInfoUseCase;
import com.account.account.applicaiton.port.in.RegisterAccountUseCase;
import com.account.account.applicaiton.port.in.UpdateAccountUseCase;
import com.account.global.config.SecurityConfig;
import com.account.global.filter.ApiCallLogFilter;
import com.account.global.filter.JwtAuthenticationFilter;
import com.account.global.util.JsonUtil;
import com.account.global.util.JwtUtil;
import com.account.token.application.port.in.RegisterTokenByRefreshUseCase;
import com.account.token.application.port.in.RegisterTokenUseCase;
import com.account.token.application.port.out.DeleteTokenPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(SecurityConfig.class)
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    protected ApiCallLogFilter apiCallLogFilter;

    @MockBean
    protected JsonUtil jsonUtil;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected FindAccountInfoUseCase findAccountInfoUseCase;

    @MockBean
    protected RegisterTokenUseCase registerTokenUseCase;

    @MockBean
    protected RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;

    @MockBean
    protected RegisterAccountUseCase registerAccountUseCase;

    @MockBean
    protected UpdateAccountUseCase updateAccountUseCase;

    @MockBean
    protected DeleteAccountUseCase deleteAccountUseCase;

    @MockBean
    protected DeleteTokenPort deleteTokenPort;
}
