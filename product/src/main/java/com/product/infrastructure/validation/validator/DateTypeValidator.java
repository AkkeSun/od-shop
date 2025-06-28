package com.product.infrastructure.validation.validator;

import com.product.infrastructure.validation.ValidDateType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class DateTypeValidator implements ConstraintValidator<ValidDateType, String> {

    private String pattern;

    @Override
    public void initialize(ValidDateType constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            if (value.contains("h") || value.contains("m") || value.contains("s")) {
                LocalDateTime.parse(value, formatter);
                return true;
            }
            if (value.contains("d")) {
                LocalDate.parse(value, formatter);
                return true;
            }

            YearMonth.parse(value, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
