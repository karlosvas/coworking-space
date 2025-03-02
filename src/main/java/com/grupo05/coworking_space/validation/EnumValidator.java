package com.grupo05.coworking_space.validation;

import com.grupo05.coworking_space.enums.RoomStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
//	private Class<? extends Enum<?>> enumClass;
	private RoomStatus[] enumValues;

	@Override
	public void initialize(ValidEnum constraintAnnotation) {
//		enumClass = constraintAnnotation.enumClass();
		this.enumValues = (RoomStatus[]) constraintAnnotation.enumClass().getEnumConstants();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return true;

		// Si el campo es un String, validar si coincide con algún nombre del enum.
//		if (value instanceof String) {
//			return Arrays.stream(enumClass.getEnumConstants()).anyMatch(e -> e
//				.name()
//				.equalsIgnoreCase((String) value));
//		}
		// Si el campo es del tipo del enum, validar si es una instancia válida.
//		return Arrays.asList(enumClass.getEnumConstants()).contains(value);
		return Arrays.stream(RoomStatus.values())
			.anyMatch(rs -> rs.getState().equalsIgnoreCase(value));
	}
}
