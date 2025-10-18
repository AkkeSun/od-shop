package com.common.infrastructure.validation.validator;

import static com.common.infrastructure.util.ValidationUtil.existsField;
import static com.common.infrastructure.util.ValidationUtil.getField;

import com.common.infrastructure.validation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (!existsField(value, "password")) {
            return true;
        }

        if (!existsField(value, "passwordCheck")) {
            return true;
        }

        if (!getField(value, "password").toString()
            .equals(getField(value, "passwordCheck").toString())) {
            context.buildConstraintViolationWithTemplate("비밀번호와 비밀번호 확인이 일치하지 않습니다.")
                .addConstraintViolation();
            return false;
        }

        return true;
    }
}
