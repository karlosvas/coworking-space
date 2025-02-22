package com.grupo05.coworking_space.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ExceptionDTO {
	private final boolean hasError = true;
	private final String message;
	private final String description;
	private final int code;
	private final Map<String, String> reasons;
	private final LocalDateTime timestamp;
}
