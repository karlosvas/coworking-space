package com.grupo05.coworking_space.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
public @interface ValidEnum {
	Class<? extends Enum<?>> enumClass();

	String message() default "{room.invalid.enum}";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
