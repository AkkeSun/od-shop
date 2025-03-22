package com.account.global.validation.validator;

import com.account.global.validation.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import org.springframework.util.StringUtils;

public class PasswordValidator  implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            Field passwordField = value.getClass().getDeclaredField("password");
            passwordField.setAccessible(true);
            String password = (String) passwordField.get(value);

            Field passwordCheckField = value.getClass().getDeclaredField("passwordCheck");
            passwordCheckField.setAccessible(true);
            String passwordCheck = (String) passwordCheckField.get(value);

            return !StringUtils.hasText(password) || !StringUtils.hasText(passwordCheck) ||
                password.equals(passwordCheck);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
