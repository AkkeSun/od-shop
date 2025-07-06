package com.product.infrastructure.validation.validator;

import com.product.domain.model.QuantityType;
import com.product.infrastructure.validation.ValidQuantityType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class QuantityTypeValidator implements ConstraintValidator<ValidQuantityType, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        try {
            QuantityType.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
