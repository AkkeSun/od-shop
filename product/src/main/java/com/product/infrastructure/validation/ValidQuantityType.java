package com.product.infrastructure.validation;

import com.product.infrastructure.validation.validator.QuantityTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = QuantityTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuantityType {

    String message() default "유효하지 않은 수정 타입 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
