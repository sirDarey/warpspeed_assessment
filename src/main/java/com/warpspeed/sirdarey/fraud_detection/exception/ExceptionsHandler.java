package com.warpspeed.sirdarey.fraud_detection.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionsHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ExceptionModel> handleCustomException(CustomException ex) {
		return ResponseEntity.status(ex.getHttpStatusCode()).body(
				new ExceptionModel(ex.getHttpStatusCode(), ex.getMessage())
		);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionModel> handleAnyException(Exception e) {
		return ResponseEntity.status(500).body(new ExceptionModel(500, e.getLocalizedMessage()));
	}
}