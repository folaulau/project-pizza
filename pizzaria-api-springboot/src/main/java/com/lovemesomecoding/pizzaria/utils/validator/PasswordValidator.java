package com.lovemesomecoding.pizzaria.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.lovemesomecoding.pizzaria.utils.ValidationUtils;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return ValidationUtils.isValidPassword(value);
    }

}
