package com.account.infrastructure.validation;

import com.account.infrastructure.validation.validator.RoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RoleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidRole {

    String message() default "유효하지 않은 권한 입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
