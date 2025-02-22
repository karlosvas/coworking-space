package com.grupo05.coworking_space.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;

import com.grupo05.coworking_space.enums.ApiError;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class RequestException extends RuntimeException {
	private final boolean hasError;
	private final String title;
	private final String detail;
	private final HttpStatus statusCode;
	private Map<String, String> reasons;
	private ApiError apiError;

	public RequestException(ApiError apiError) {
		this.hasError = true;
		this.title = apiError.getTitle();
		this.detail = apiError.getDetail();
		this.statusCode = apiError.getStatus();
		this.apiError = apiError;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}

	public RequestException(
			String title,
			String detail,
			HttpStatus statusCode,
			Map<String, String> reasons) {
		this.hasError = true;
		this.title = title;
		this.detail = detail;
		this.statusCode = statusCode;
		this.reasons = reasons;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}

	public RequestException(ApiError apiError, String title, String detail) {
		this.hasError = true;
		this.title = title;
		this.detail = detail;
		this.statusCode = apiError.getStatus();
		this.apiError = apiError;
		log.error("ID error ReservationNotFoundException: {}", apiError);
	}
}
