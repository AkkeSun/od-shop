package com.account.adapter.in.controller.delete_token;

import com.account.applicaiton.service.delete_token.DeleteTokenServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class DeleteTokenResponse {

    private String result;

    @Builder
    DeleteTokenResponse(String result) {
        this.result = result;
    }

    DeleteTokenResponse of(DeleteTokenServiceResponse serviceResponse) {
        return DeleteTokenResponse.builder()
            .result(serviceResponse.result())
            .build();
    }
}
