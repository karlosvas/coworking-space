package com.grupo05.coworking_space.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
	private boolean hasError;
	private String title;
	private String detail;
	private int statusCode;
}
