package com.product.infrastructure.validation.validator;

import com.product.domain.model.Category;
import com.product.infrastructure.validation.ValidCategory;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<ValidCategory, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return true;
        }
        try {
            Category.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
