package com.product.infrastructure.validation.validator;

import com.product.domain.model.SortType;
import com.product.infrastructure.validation.ValidSortType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortTypeValidator implements ConstraintValidator<ValidSortType, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return true;
        }
        try {
            SortType.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
