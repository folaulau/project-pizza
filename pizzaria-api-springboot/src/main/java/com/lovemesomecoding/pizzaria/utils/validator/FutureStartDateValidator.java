package com.lovemesomecoding.pizzaria.utils.validator;

import java.util.Date;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
public class FutureStartDateValidator implements ConstraintValidator<FutureStartDate, Date> {

	@Override
	public boolean isValid(Date value, ConstraintValidatorContext context) {
		
		return value.after(new Date());
	}

}
