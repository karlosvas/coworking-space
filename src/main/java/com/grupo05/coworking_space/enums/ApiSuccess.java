package com.grupo05.coworking_space.enums;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApiSuccess {
	RESOURCE_RETRIEVED("Resource Retrieved", HttpStatus.OK),
	RESOURCE_CREATED("Resource Created", HttpStatus.CREATED),
	RESOURCE_UPDATED("Resource Updated", HttpStatus.OK),
	RESOURCE_REMOVED("Resource Removed", HttpStatus.NO_CONTENT),
	RESOURCE_NO_CONTENT("Resource No Content", HttpStatus.NO_CONTENT);

	private final String message;
	private final HttpStatus status;
}
