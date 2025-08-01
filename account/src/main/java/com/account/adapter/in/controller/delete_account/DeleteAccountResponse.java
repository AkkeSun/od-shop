package com.account.adapter.in.controller.delete_account;

import com.account.applicaiton.service.delete_account.DeleteAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class DeleteAccountResponse {

    private Long id;
    private String result;

    @Builder
    DeleteAccountResponse(Long id, String result) {
        this.id = id;
        this.result = result;
    }

    static DeleteAccountResponse of(DeleteAccountServiceResponse serviceResponse) {
        return DeleteAccountResponse.builder()
            .id(serviceResponse.id())
            .result(serviceResponse.result())
            .build();
    }
}
