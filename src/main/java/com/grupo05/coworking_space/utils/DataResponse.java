package com.grupo05.coworking_space.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

import com.grupo05.coworking_space.enums.ApiSuccess;

import lombok.Getter;

@Getter
public class DataResponse {
	private final Object data;
	private final boolean hasError;
	private final String message;
	private final int statusCode;
	private final LocalDateTime timestamp;

	public DataResponse(ApiSuccess apiSuccess, Object data) {
		this.data = data;
		this.hasError = false;
		this.message = apiSuccess.getMessage();
		this.statusCode = apiSuccess.getStatus().value();
		this.timestamp = LocalDateTime.now(ZoneId.systemDefault());
	}
}
