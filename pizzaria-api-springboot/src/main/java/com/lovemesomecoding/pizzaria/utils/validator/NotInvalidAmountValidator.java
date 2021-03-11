package com.lovemesomecoding.pizzaria.utils.validator;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.time.DateUtils;

public class NotInvalidAmountValidator implements ConstraintValidator<NotInvalidAmount, Double> {

	@Override
	public boolean isValid(Double amount, ConstraintValidatorContext context) {

		if (amount == null) {
			return false;
		}
		
		return true;
	}

}
