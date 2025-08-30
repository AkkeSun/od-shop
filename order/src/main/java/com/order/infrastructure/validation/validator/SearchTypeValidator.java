package com.order.infrastructure.validation.validator;

import com.order.infrastructure.validation.ValidSearchType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SearchTypeValidator implements ConstraintValidator<ValidSearchType, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return true;
        }
        return input.equals("customerId") || input.equals("productId") || input.equals("buyStatus");
    }
}
