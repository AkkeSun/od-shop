package com.order.infrastructure.validation;

import com.order.infrastructure.validation.validator.SearchTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SearchTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSearchType {

    String message() default "유효하지 않은 검색 조건 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
