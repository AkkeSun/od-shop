package com.account.applicaiton.service.delete_token;

import lombok.Builder;

@Builder
public record DeleteTokenServiceResponse(
    String result
) {

    public static DeleteTokenServiceResponse ofSuccess() {
        return DeleteTokenServiceResponse.builder()
            .result("Y")
            .build();
    }
}
