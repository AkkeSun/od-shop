package com.account.infrastructure.validation.validator;

import com.account.infrastructure.validation.ValidUserTel;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.springframework.util.StringUtils;

public class UserTelValidator implements ConstraintValidator<ValidUserTel, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (!StringUtils.hasText(value)) {
            return true;
        }
        return Pattern.compile("^01[016789]\\d{7,8}$").matcher(value).matches();
    }
}
