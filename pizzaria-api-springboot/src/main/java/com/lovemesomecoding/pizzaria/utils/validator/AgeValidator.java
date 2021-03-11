package com.lovemesomecoding.pizzaria.utils.validator;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.time.DateUtils;

public class AgeValidator implements ConstraintValidator<Age, Date> {

	@Override
	public boolean isValid(Date date, ConstraintValidatorContext context) {

		if (date == null) {
			return false;
		}

		try {

			Date MAX_AGE_REQUIREMENT_DATE = DateUtils.addYears(new Date(), -150);

			Date MIN_AGE_REQUIREMENT_DATE = DateUtils.addMilliseconds(new Date(), 0);

			if (date.after(MIN_AGE_REQUIREMENT_DATE)) {
				return false;
			}

			if (date.before(MAX_AGE_REQUIREMENT_DATE)) {
				return false;
			}

			return true;
		} catch (Exception e) {
			System.out.println("AgeValidator - Exception, msg: " + e.getLocalizedMessage());
			return false;
		}
	}

}
