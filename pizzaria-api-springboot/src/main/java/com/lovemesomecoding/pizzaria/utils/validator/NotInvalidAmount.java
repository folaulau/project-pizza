package com.lovemesomecoding.pizzaria.utils.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotInvalidAmountValidator.class)
public @interface NotInvalidAmount {

	String message() default "not a valid amount";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
