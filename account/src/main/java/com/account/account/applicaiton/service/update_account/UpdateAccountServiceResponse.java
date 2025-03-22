package com.account.account.applicaiton.service.update_account;

import java.util.List;
import lombok.Builder;

@Builder
public record UpdateAccountServiceResponse(
    String updateYn,
    List<String> updateList
) {

}
