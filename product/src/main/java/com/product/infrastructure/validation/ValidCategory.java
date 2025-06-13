package com.product.infrastructure.validation;

import com.product.infrastructure.validation.validator.CategoryValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CategoryValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {

    String message() default "존재하지 않은 카테고리 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
