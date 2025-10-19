package com.product.adapter.in.controller.find_recommend_product;

import static com.common.infrastructure.util.JsonUtil.toJsonString;

import com.common.infrastructure.resolver.LoginAccountInfo;
import com.common.infrastructure.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.application.port.in.command.FindRecommendProductCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
class FindRecommendProductRequest {

    @NotBlank(message = "검색 날짜는 필수값 입니다", groups = NotBlankGroups.class)
    private String searchDate;

    FindRecommendProductCommand toCommand(LoginAccountInfo loginInfo) {
        return FindRecommendProductCommand.builder()
            .accountId(loginInfo == null ? null : loginInfo.getId())
            .searchDate(searchDate)
            .build();
    }

    @Override
    public String toString() {
        return toJsonString(this);
    }
}
