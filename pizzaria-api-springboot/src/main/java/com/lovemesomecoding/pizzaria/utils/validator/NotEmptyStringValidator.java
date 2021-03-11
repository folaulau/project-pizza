package com.lovemesomecoding.pizzaria.utils.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotEmptyStringValidator implements ConstraintValidator<NotEmptyString, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value==null) {
			return false;
		}
		if(value.trim().length()<=0) {
			return false;
		}
		return true;
	}
}
