package com.account.applicaiton.service.update_account;

import java.util.List;
import lombok.Builder;

@Builder
public record UpdateAccountServiceResponse(
    String updateYn,
    List<String> updateList
) {

    public static UpdateAccountServiceResponse ofSuccess(List<String> updateList) {
        return UpdateAccountServiceResponse.builder()
            .updateYn("Y")
            .updateList(updateList)
            .build();
    }

    public static UpdateAccountServiceResponse ofFailure(List<String> updateList) {
        return UpdateAccountServiceResponse.builder()
            .updateYn("N")
            .updateList(updateList)
            .build();
    }
}
