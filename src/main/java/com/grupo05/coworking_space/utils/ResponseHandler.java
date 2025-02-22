package com.grupo05.coworking_space.utils;

import org.springframework.http.ResponseEntity;

import com.grupo05.coworking_space.enums.ApiSuccess;

public class ResponseHandler {
	public static ResponseEntity<DataResponse> handleApiResponse(ApiSuccess apiSuccess, Object data) {
		DataResponse apiResponse = new DataResponse(apiSuccess, data);
		return new ResponseEntity<>(apiResponse, apiSuccess.getStatus());
	}
}
