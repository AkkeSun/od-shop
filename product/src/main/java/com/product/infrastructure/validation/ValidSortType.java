package com.product.infrastructure.validation;

import com.product.infrastructure.validation.validator.SortTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SortTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSortType {

    String message() default "존재하지 않은 정렬타입 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
