package com.account.infrastructure.validation.validator;

import static com.account.domain.model.Role.ROLE_CUSTOMER;
import static com.account.domain.model.Role.ROLE_SELLER;

import com.account.infrastructure.validation.ValidRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.equals(ROLE_CUSTOMER.name()) || value.equals(ROLE_SELLER.name());
    }
}
