package com.common.infrastructure.validation;

import com.common.infrastructure.validation.validator.UserTelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserTelValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUserTel {

    String message() default "올바른 전화번호 형식이 아닙니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
