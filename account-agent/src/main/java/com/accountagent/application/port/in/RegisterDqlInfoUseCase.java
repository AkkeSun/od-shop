package com.accountagent.application.port.in;

import com.accountagent.application.service.register_dql_info.RegisterDqlInfoServiceResponse;

public interface RegisterDqlInfoUseCase {

    RegisterDqlInfoServiceResponse registerDqlInfo(String payload);
}
