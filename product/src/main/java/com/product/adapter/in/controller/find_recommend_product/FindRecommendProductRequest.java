package com.product.adapter.in.controller.find_recommend_product;

import com.product.application.port.in.command.FindRecommendProductCommand;
import com.product.domain.model.Account;
import com.product.infrastructure.validation.ValidDateType;
import com.product.infrastructure.validation.groups.ValidationGroups.CustomGroups;
import com.product.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindRecommendProductRequest {

    @ValidDateType(groups = CustomGroups.class)
    @NotBlank(message = "검색 날짜는 필수값 입니다", groups = NotBlankGroups.class)
    private String searchDate;

    FindRecommendProductCommand toCommand(Account account) {
        return FindRecommendProductCommand.builder()
            .accountId(account.id())
            .searchDate(searchDate)
            .build();
    }
}
