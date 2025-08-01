package com.account.adapter.in.controller.update_account;

import com.account.applicaiton.service.update_account.UpdateAccountServiceResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class UpdateAccountResponse {

    private String updateYn;
    private List<String> updateList;

    @Builder
    UpdateAccountResponse(String updateYn, List<String> updateList) {
        this.updateYn = updateYn;
        this.updateList = updateList;
    }

    static UpdateAccountResponse of(UpdateAccountServiceResponse serviceResponse) {
        return UpdateAccountResponse.builder()
            .updateYn(serviceResponse.updateYn())
            .updateList(serviceResponse.updateList())
            .build();
    }
}
